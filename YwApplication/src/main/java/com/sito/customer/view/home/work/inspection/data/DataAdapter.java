package com.sito.customer.view.home.work.inspection.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.mode.bean.work.InspectionDataBean;
import com.sito.customer.view.photo.ViewPagePhotoActivity;
import com.sito.customer.widget.InspectionType1;
import com.sito.customer.widget.InspectionType2;
import com.sito.library.utils.GlideUtils;

import java.util.List;

/**
 * 巡检录入界面数据adapter
 * Created by zhangan on 2017-07-27.
 */
class DataAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<InspectionDataBean.RoomListBean> dataList;


    DataAdapter(Context mContext, List<InspectionDataBean.RoomListBean> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(groupPosition).getTaskEquipment().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).getTaskEquipment().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_inspection_data, null);
            holder.bgLayout = convertView.findViewById(R.id.ll_item_group);
            holder.tv_room_name = convertView.findViewById(R.id.tv_room_name);
            holder.iv_state = convertView.findViewById(R.id.iv_state);
            holder.tv_equipment_count = convertView.findViewById(R.id.tv_equipment_count);
            holder.division = convertView.findViewById(R.id.division_id);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tv_room_name.setText(dataList.get(groupPosition).getRoom().getRoomName());
        holder.tv_equipment_count.setText(String.valueOf(getChildrenCount(groupPosition)));
        if (dataList.size() > 1) {
            if (groupPosition == 0) {
                if (isExpanded) {
                    holder.division.setVisibility(View.GONE);
                } else {
                    holder.division.setVisibility(View.VISIBLE);
                }
            } else if (groupPosition == dataList.size() - 1) {
                holder.division.setVisibility(View.GONE);
            } else {
                if (isExpanded) {
                    holder.division.setVisibility(View.GONE);
                } else {
                    holder.division.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (isExpanded) {
                holder.division.setVisibility(View.GONE);
            } else {
                holder.division.setVisibility(View.GONE);
            }
        }
        if (isExpanded) {
            holder.iv_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_employee_arrow_open));
        } else {
            holder.iv_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_employee_arrow));
        }
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_inspection_data, null);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_equipment = convertView.findViewById(R.id.tv_equipment);
            holder.ll_pic = convertView.findViewById(R.id.ll_pic);
            holder.iv_pic = convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        if (dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList() == null ||
                dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList().size() == 0) {
            holder.ll_pic.setVisibility(View.GONE);
        } else {
            holder.ll_pic.setVisibility(View.VISIBLE);
            GlideUtils.ShowImage(mContext, dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment()
                            .getInPlacePicList().get(0).getInplacePicUrl()
                    , holder.iv_pic, R.drawable.picture_default);
        }
        holder.ll_pic.setTag(R.id.tag_position, groupPosition);
        holder.ll_pic.setTag(R.id.tag_position_1, childPosition);
        holder.ll_pic.setOnClickListener(showPicListener);
        holder.ll_item.removeAllViews();
        holder.tv_equipment.setText(dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getEquipmentName());
        List<InspectionDataBean.RoomListBean.TaskEquipmentBean.DataListBean.DataItemValueListBean> listBeen =
                dataList.get(groupPosition).getTaskEquipment().get(childPosition).getDataList().get(0).getDataItemValueList();
        for (int i = 0; i < listBeen.size(); i++) {
            String value = listBeen.get(i).getValue();
            String inspectionName = listBeen.get(i).getDataItem().getInspectionName();
            int inspectionType = listBeen.get(i).getDataItem().getInspectionType();
            if (inspectionType == 3) {
                holder.ll_item.addView(new InspectionType2(mContext).setValue(inspectionName, value));
            } else {
                holder.ll_item.addView(new InspectionType1(mContext).setValue(inspectionName, value));
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private class GroupViewHolder {
        TextView tv_room_name;
        TextView tv_equipment_count;
        ImageView iv_state;
        LinearLayout bgLayout;
        View division;
    }

    private class ChildViewHolder {
        TextView tv_equipment;
        LinearLayout ll_item;
        LinearLayout ll_pic;
        ImageView iv_pic;
    }

    private View.OnClickListener showPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int groupPosition = (int) v.getTag(R.id.tag_position);
            int childPosition = (int) v.getTag(R.id.tag_position_1);
            if (dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList() != null &&
                    dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList().size() != 0) {
                String[] url = new String[dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList().size()];
                for (int i = 0; i < dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList().size(); i++) {
                    url[i] = dataList.get(groupPosition).getTaskEquipment().get(childPosition).getEquipment().getInPlacePicList().get(i).getInplacePicUrl();
                }
                ViewPagePhotoActivity.startActivity(mContext, url, 0);
            }
        }
    };
}
