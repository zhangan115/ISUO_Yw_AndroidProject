package com.isuo.yw2application.view.main.work.fire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.equip.EquipName;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.fire.FireBean;
import com.isuo.yw2application.mode.fire.FireListBean;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhangan on 2017/7/1 11:39
 */
public class FireListAdapter extends BaseExpandableListAdapter {
    //展示页面
    private PinnedHeaderExpandableListView mExpandListView;
    private Context context;
    private int groupLayout, childLayout;
    private List<FireListBean> data = new ArrayList<>();
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private ItemClickListener listener;

    interface ItemClickListener {

        void onItemClick(FireListBean equipBean, FireBean equipName);

    }

    public void setItemListener(ItemClickListener listener) {
        this.listener = listener;
    }

    FireListAdapter(Context context, PinnedHeaderExpandableListView expandableListView, int groupLayout, int childLayout) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.mExpandListView = expandableListView;
    }

    public void setData(List<FireListBean> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getEquipments().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getEquipments().get(childPosition);
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

    @SuppressLint("DefaultLocale")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(groupLayout, null);
            holder.mGroup = (LinearLayout) convertView.findViewById(R.id.ll_item_group);
            holder.mPlace = (TextView) convertView.findViewById(R.id.id_item_equip_place);
            holder.mCount = (TextView) convertView.findViewById(R.id.id_item_equip_mum);
            holder.stateIv = (ImageView) convertView.findViewById(R.id.iv_state);
            holder.mLine = convertView.findViewById(R.id.id_item_equip_line);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.mPlace.setText(data.get(groupPosition).getRoomName());
        holder.mCount.setText(String.format("%d", data.get(groupPosition).getEquipments().size()));
        holder.mLine.setBackgroundColor(context.getResources().getColor(R.color.setting_text_job));
        if (data.size() > 1) {
            if (groupPosition == 0) {
                holder.mLine.setVisibility(View.VISIBLE);
            } else if (groupPosition == data.size() - 1) {
                holder.mLine.setVisibility(View.GONE);
            } else {
                holder.mLine.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mLine.setVisibility(View.GONE);
        }
        if (isExpanded) {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow_open));
        } else {
            holder.stateIv.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow));
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(childLayout, null);
            holder.mName = (TextView) convertView.findViewById(R.id.id_item_equip_name);
            holder.mLine = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.mLine.setBackgroundColor(context.getResources().getColor(R.color.setting_text_job));
        holder.mName.setText(String.format("%s%s", data.get(groupPosition).getEquipments().get(childPosition).getEquipmentName()
                , TextUtils.isEmpty(data.get(groupPosition).getEquipments().get(childPosition).getEquipmentSn()) ? ""
                        : "(" + data.get(groupPosition).getEquipments().get(childPosition).getEquipmentSn() + ")"));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }
                listener.onItemClick(data.get(groupPosition), data.get(groupPosition).getEquipments().get(childPosition));
            }
        });
        return convertView;
    }


    interface OnClickListener {
        void onClick(EquipmentBean equipmentBean);
    }

    interface OnItemClickListener {
        void onClick(EquipName equipName);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 外部显示ViewHolder
     */
    private static class GroupViewHolder {
        LinearLayout mGroup;
        View mLine;
        TextView mPlace;
        TextView mCount;
        ImageView stateIv;
    }

    /**
     * 内部显示ViewHolder
     */
    private static class ChildViewHolder {
        TextView mName;
        View mLine;
        LinearLayout mChild;
    }
}
