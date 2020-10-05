package com.isuo.yw2application.view.main.adduser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * equipment list show adapter
 * Created by zhangan on 2016-09-19.
 */
class EmployeeListAdapter extends BaseExpandableListAdapter {
    //展示页面
    private Context context;
    private int groupLayout, childLayout;
    private List<DepartmentBean> data = new ArrayList<>();
    private boolean isChooseOne;
    private boolean canChooseMySelf;


    EmployeeListAdapter(Context context, int groupLayout, int childLayout, boolean isChooseOne) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.isChooseOne = isChooseOne;
    }

    public void setData(List<DepartmentBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    void setCanChooseMySelf(boolean canChooseMySelf) {
        this.canChooseMySelf = canChooseMySelf;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getUserList() == null) {
            return 0;
        }
        return data.get(groupPosition).getUserList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (data.get(groupPosition).getUserList() == null) {
            return 0;
        }
        return data.get(groupPosition).getUserList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(groupLayout, null);
            holder.bgLayout = convertView.findViewById(R.id.ll_item_group);
            holder.division = convertView.findViewById(R.id.division_id);
            holder.stateIv = convertView.findViewById(R.id.iv_state);
            holder.nameTv = convertView.findViewById(R.id.tv_name);
            holder.countTv = convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (data.size() > 1) {
            if (groupPosition == 0) {
                holder.division.setVisibility(View.VISIBLE);
            } else if (groupPosition == data.size() - 1) {
                holder.division.setVisibility(View.GONE);
            } else {
                holder.division.setVisibility(View.VISIBLE);
            }
        } else {
            holder.division.setVisibility(View.GONE);
        }
        if (isExpanded) {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow_open));
        } else {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow));
        }
        holder.nameTv.setText(data.get(groupPosition).getDeptName());
        if (data.get(groupPosition).getUserList() != null) {
            holder.countTv.setText(String.valueOf(data.get(groupPosition).getUserList().size()));
        } else {
            holder.countTv.setText(String.valueOf(0));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(childLayout, null);
            holder.nameTv = convertView.findViewById(R.id.tv_name);
            holder.ll_child_layout = convertView.findViewById(R.id.ll_child_layout);
            holder.stateIv = convertView.findViewById(R.id.iv_state);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(data.get(groupPosition).getUserList().get(childPosition).getUser().getRealName());
        if (data.get(groupPosition).getUserList().get(childPosition).isSelect()) {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_select));
        } else {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_normal));
        }
        holder.ll_child_layout.setTag(R.id.tag_group_position, groupPosition);
        holder.ll_child_layout.setTag(R.id.tag_child_position, childPosition);
        holder.ll_child_layout.setOnClickListener(onClickListener);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 外部显示ViewHolder
     */
    private class GroupViewHolder {
        LinearLayout bgLayout;
        View division;
        ImageView stateIv;
        TextView nameTv;
        TextView countTv;
    }

    /**
     * 内部显示ViewHolder
     */
    private class ChildViewHolder {
        TextView nameTv;
        ImageView stateIv;
        LinearLayout ll_child_layout;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int groupPosition = (int) v.getTag(R.id.tag_group_position);
            int childPosition = (int) v.getTag(R.id.tag_child_position);
            EmployeeBean employ = data.get(groupPosition).getUserList().get(childPosition);
            boolean isSelect = !employ.isSelect();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getUserList() == null || data.get(i).getUserList().size() == 0) {
                    continue;
                }
                for (int j = 0; j < data.get(i).getUserList().size(); j++) {
                    if (isChooseOne) {
                        if (data.get(i).getUserList().get(j).getUser().getUserId() == employ.getUser().getUserId()) {
                            data.get(i).getUserList().get(j).setSelect(isSelect);
                        } else {
                            data.get(i).getUserList().get(j).setSelect(false);
                        }
                    } else {
                        if (data.get(i).getUserList().get(j).getUser().getUserId() == employ.getUser().getUserId()) {
                            if (canChooseMySelf && employ.getUser().getUserId() == Yw2Application.getInstance().getCurrentUser().getUserId()) {
                                data.get(i).getUserList().get(j).setSelect(true);
                            } else {
                                data.get(i).getUserList().get(j).setSelect(isSelect);
                            }
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    };

    ArrayList<EmployeeBean> getSelectEmployee() {
        ArrayList<EmployeeBean> employeeBeen = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getUserList() == null) {
                continue;
            }
            for (int j = 0; j < data.get(i).getUserList().size(); j++) {
                if (data.get(i).getUserList().get(j).isSelect()) {
                    boolean needAdd = true;
                    for (int k = 0; k < employeeBeen.size(); k++) {
                        if (employeeBeen.get(k).getUser().getUserId() == data.get(i).getUserList().get(j).getUser().getUserId()) {
                            needAdd = false;
                        }
                    }
                    if (needAdd) {
                        employeeBeen.add(data.get(i).getUserList().get(j));
                    }
                }
            }
        }
        return employeeBeen;
    }
}
