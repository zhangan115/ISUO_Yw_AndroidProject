package com.isuo.yw2application.view.main.task.inspection;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.bean.work.InspectionRegionModel;
import com.isuo.yw2application.view.main.task.inspection.security.SecurityPackageActivity;
import com.isuo.yw2application.view.main.task.inspection.work.InspectionRoomActivity;
import com.orhanobut.logger.Logger;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.SPHelper;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class WorkInspectionAdapter extends BaseExpandableListAdapter {

    //展示页面
    private PinnedHeaderExpandableListView mExpandListView;
    private Context context;
    private int groupLayout, childLayout;
    private List<InspectionRegionModel> data = new ArrayList<>();

    private int[] icons = new int[]{R.drawable.work_day_icon
            , R.drawable.work_week_icon
            , R.drawable.work_month_icon
            , R.drawable.work_special_icon};
    private String[] inspectionTypeStr = new String[]{"日检", "周检", "月检", "特检"};

    private ItemClickListener listener;

    interface ItemClickListener {

        void onItemClick(InspectionBean inspectionBean);

        void operationTask(String id, int groupPosition, int childPosition);

        void toStartActivity(long taskId, long securityId);


    }

    public void setItemListener(ItemClickListener listener) {
        this.listener = listener;
    }

    WorkInspectionAdapter(Context context, PinnedHeaderExpandableListView expandableListView, int groupLayout, int childLayout) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.mExpandListView = expandableListView;
    }

    public void setData(List<InspectionRegionModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getInspectionBeanList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getInspectionBeanList().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(groupLayout, null);
            holder.mGroup = (LinearLayout) convertView.findViewById(R.id.ll_item_group);
            holder.mPlace = (TextView) convertView.findViewById(R.id.id_item_equip_place);
            holder.mCount = (TextView) convertView.findViewById(R.id.id_item_equip_mum);
            holder.unitTv = (TextView) convertView.findViewById(R.id.unitTv);
            holder.stateIv = (ImageView) convertView.findViewById(R.id.iv_state);
            holder.mLine = convertView.findViewById(R.id.id_item_equip_line);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.mPlace.setText(data.get(groupPosition).getRegionName());
        holder.mCount.setVisibility(View.GONE);
        holder.unitTv.setVisibility(View.VISIBLE);
        int notFinishCount = 0;
        int taskCount = 0;
        if (data.get(groupPosition).getInspectionBeanList() != null) {
            for (int i = 0; i < data.get(groupPosition).getInspectionBeanList().size(); i++) {
                if (data.get(groupPosition).getInspectionBeanList().get(i).getTaskState() < ConstantInt.TASK_STATE_4) {
                    notFinishCount++;
                }
            }
            taskCount = data.get(groupPosition).getInspectionBeanList().size();
        }
        holder.unitTv.setText(MessageFormat.format("总数:{0} 未完成:{1}", taskCount, notFinishCount));
        if (isExpanded) {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow_open));
        } else {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow));
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder vHolder;
        InspectionBean data = (InspectionBean) getChild(groupPosition, childPosition);
        if (convertView == null) {
            vHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(childLayout, null);
            vHolder.tv_belong_place = (TextView) convertView.findViewById(R.id.tv_belong_place);
            vHolder.tv_task_name = (TextView) convertView.findViewById(R.id.tv_task_name);
            vHolder.tv_equip_num = (TextView) convertView.findViewById(R.id.tv_equip_num);
            vHolder.tv_time_plan = (TextView) convertView.findViewById(R.id.tv_time_plan_start);
            vHolder.tv_time_actual = (TextView) convertView.findViewById(R.id.tv_time_actual_start);
            vHolder.tv_executor_user_type = (TextView) convertView.findViewById(R.id.tv_executor_user_type);
            vHolder.tv_time_plan_end = (TextView) convertView.findViewById(R.id.tv_time_plan_end);
            vHolder.tv_time_actual_end = (TextView) convertView.findViewById(R.id.tv_time_actual_end);
            vHolder.planStartTimeTv = (TextView) convertView.findViewById(R.id.planStartTimeTv);
            vHolder.actualStartTimeTv = (TextView) convertView.findViewById(R.id.actualStartTimeTv);
            vHolder.startTaskTv = (TextView) convertView.findViewById(R.id.startTaskTv);
            vHolder.ll_inspection_type = (LinearLayout) convertView.findViewById(R.id.ll_inspection_type);
            vHolder.ll_actual_time = (LinearLayout) convertView.findViewById(R.id.ll_actual_time);
            vHolder.iv_inspection_type = (ImageView) convertView.findViewById(R.id.iv_inspection_type);
            vHolder.tv_inspection_type = (TextView) convertView.findViewById(R.id.tv_inspection_type);
            vHolder.tv_executor_inspection_user = (TextView) convertView.findViewById(R.id.tv_executor_inspection_user);
            vHolder.startTaskLayout = (LinearLayout) convertView.findViewById(R.id.ll_start_task);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ChildViewHolder) convertView.getTag();
        }
        if (data.getIsManualCreated() == 0) {
            if (data.getPlanPeriodType() == 0) {
                vHolder.ll_inspection_type.setVisibility(View.GONE);
            } else {
                vHolder.ll_inspection_type.setVisibility(View.VISIBLE);
                vHolder.iv_inspection_type.setImageDrawable(context.getDrawable(icons[data.getPlanPeriodType() - 1]));
                vHolder.tv_inspection_type.setText(inspectionTypeStr[data.getPlanPeriodType() - 1]);
            }
        } else {
            vHolder.ll_inspection_type.setVisibility(View.VISIBLE);
            vHolder.iv_inspection_type.setImageDrawable(context.getDrawable(icons[3]));
            vHolder.tv_inspection_type.setText(inspectionTypeStr[3]);
        }
        vHolder.tv_task_name.setText(data.getTaskName());
        if (data.getTaskState() == ConstantInt.TASK_STATE_1) {
            vHolder.ll_actual_time.setVisibility(View.GONE);
            if (data.getExecutorUserList() != null && data.getExecutorUserList().size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.getExecutorUserList().size(); i++) {
                    if (data.getExecutorUserList().get(i).getUser() == null) {
                        continue;
                    }
                    if (!TextUtils.isEmpty(data.getExecutorUserList().get(i).getUser().getRealName())) {
                        sb.append(data.getExecutorUserList().get(i).getUser().getRealName());
                        if (i != data.getExecutorUserList().size() - 1) {
                            sb.append("、");
                        }
                    }
                }
                vHolder.tv_executor_inspection_user.setText(sb.toString());
            } else {
                vHolder.tv_executor_inspection_user.setText("");
            }
            vHolder.tv_executor_user_type.setText("被指派人:");
            vHolder.actualStartTimeTv.setText("实际开始:");
            vHolder.startTaskTv.setText("领取任务");
        } else if (data.getTaskState() == ConstantInt.TASK_STATE_2) {
            vHolder.ll_actual_time.setVisibility(View.GONE);
            vHolder.tv_executor_user_type.setText("领 取 人:");
            if (data.getReceiveUser() != null) {
                vHolder.tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
            } else {
                vHolder.tv_executor_inspection_user.setText("");
            }
            vHolder.actualStartTimeTv.setText("实际开始:");
            vHolder.startTaskTv.setText("开始任务");
        } else if (data.getTaskState() == ConstantInt.TASK_STATE_3) {
            vHolder.ll_actual_time.setVisibility(View.GONE);
            vHolder.tv_executor_user_type.setText("领 取 人:");
            if (data.getReceiveUser() != null) {
                vHolder.tv_executor_inspection_user.setText(data.getReceiveUser().getRealName());
            } else {
                vHolder.tv_executor_inspection_user.setText("");
            }
            vHolder.actualStartTimeTv.setText("实际开始:");
            vHolder.startTaskTv.setText("开始任务");
        } else if (data.getTaskState() == ConstantInt.TASK_STATE_4) {
            vHolder.actualStartTimeTv.setText("巡检截至:");
            vHolder.ll_actual_time.setVisibility(View.VISIBLE);
            vHolder.tv_time_actual.setText(DataUtil.timeFormat(data.getStartTime(), "yyyy-MM-dd HH:mm"));
            vHolder.tv_time_actual_end.setText(DataUtil.timeFormat(data.getEndTime(), "yyyy-MM-dd HH:mm"));
            StringBuilder sb = new StringBuilder();
            if (data.getUsers() != null) {
                for (int i = 0; i < data.getUsers().size(); i++) {
                    if (data.getUsers().get(i) == null) {
                        continue;
                    }
                    sb.append(data.getUsers().get(i).getRealName());
                    if (i != data.getUsers().size() - 1) {
                        sb.append("、");
                    }
                }
            }
            vHolder.tv_executor_inspection_user.setText(sb.toString());
            vHolder.tv_executor_user_type.setText("执 行 人:");
            vHolder.startTaskTv.setText("开始任务");
        }
        StringBuilder sb = new StringBuilder();
        if (data.getRooms() != null) {
            for (int i = 0; i < data.getRooms().size(); i++) {
                sb.append(data.getRooms().get(i));
                if (i != data.getRooms().size() - 1) {
                    sb.append("、");
                }
            }
        }
        vHolder.tv_belong_place.setText(sb.toString());
        String str = data.getUploadCount() + "/" + data.getCount();
        vHolder.tv_equip_num.setText(str);
        if (data.getPlanStartTime() != 0) {
            vHolder.tv_time_plan.setVisibility(View.VISIBLE);
            vHolder.planStartTimeTv.setText("计划起止:");
            vHolder.tv_time_plan.setText(MessageFormat.format("{0}"
                    , DataUtil.timeFormat(data.getPlanStartTime(), "yyyy-MM-dd HH:mm")));
        } else {
            vHolder.planStartTimeTv.setVisibility(View.GONE);
            vHolder.tv_time_plan.setVisibility(View.GONE);
        }
        if (data.getPlanEndTime() != 0) {
            vHolder.tv_time_plan_end.setVisibility(View.VISIBLE);
            vHolder.tv_time_plan_end.setText(DataUtil.timeFormat(data.getPlanEndTime()
                    , "yyyy-MM-dd HH:mm"));
        } else {
            vHolder.tv_time_plan_end.setVisibility(View.GONE);
        }
        if (data.getTaskState() == ConstantInt.TASK_STATE_4) {
            vHolder.startTaskLayout.setVisibility(View.GONE);
        } else {
            vHolder.startTaskLayout.setVisibility(View.VISIBLE);
        }
        vHolder.startTaskLayout.setTag(R.id.tag_position_2, groupPosition);
        vHolder.startTaskLayout.setTag(R.id.tag_position, childPosition);
        vHolder.startTaskLayout.setTag(R.id.tag_task, data.getTaskId());
        vHolder.startTaskLayout.setTag(R.id.tag_position_1, data.getSecurityPackage() == null ? -1L : data.getSecurityPackage().getSecurityId());
        vHolder.startTaskLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }
                int groupPosition = (int) v.getTag(R.id.tag_position_2);
                int childPosition = (int) v.getTag(R.id.tag_position);
                if (data.getTaskState() == ConstantInt.TASK_STATE_1) {
                    listener.operationTask(String.valueOf(data.getTaskId()), groupPosition, childPosition);
                    return;
                }
                long taskId = (long) v.getTag(R.id.tag_task);
                long securityId = (long) v.getTag(R.id.tag_position_1);
                listener.toStartActivity(taskId, securityId);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }
                listener.onItemClick(WorkInspectionAdapter.this.data.get(groupPosition).getInspectionBeanList().get(childPosition));
            }
        });
        return convertView;
    }


    interface OnItemClickListener {
        void onClick(InspectionBean equipName);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 外部显示ViewHolder
     */
    private class GroupViewHolder {
        LinearLayout mGroup;
        View mLine;
        TextView mPlace;
        TextView mCount;
        TextView unitTv;
        ImageView stateIv;
    }

    /**
     * 内部显示ViewHolder
     */
    private class ChildViewHolder {
        TextView mName;
        View mLine;
        LinearLayout mChild;
        TextView tv_belong_place;
        TextView tv_task_name;
        TextView tv_equip_num;
        TextView tv_time_plan;
        TextView tv_time_actual;
        TextView tv_executor_user_type;
        TextView tv_time_plan_end;
        TextView tv_time_actual_end;
        TextView planStartTimeTv;
        TextView actualStartTimeTv;
        TextView startTaskTv;
        LinearLayout ll_inspection_type;
        LinearLayout ll_actual_time;
        ImageView iv_inspection_type;
        TextView tv_inspection_type;
        TextView tv_executor_inspection_user;
        LinearLayout startTaskLayout;
    }
}
