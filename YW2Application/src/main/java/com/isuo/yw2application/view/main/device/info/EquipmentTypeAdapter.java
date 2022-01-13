package com.isuo.yw2application.view.main.device.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.sito.library.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class EquipmentTypeAdapter extends BaseExpandableListAdapter {

    //展示页面
    private PinnedHeaderExpandableListView mExpandListView;
    private Context context;
    private int groupLayout, childLayout;
    private List<ChooseRoomOrType> dataList = new ArrayList<>();


    private ItemClickListener listener;

    interface ItemClickListener {

        void onItemClick(ChooseRoomOrType chooseRoomOrType);

    }

    public void setItemListener(ItemClickListener listener) {
        this.listener = listener;
    }

    EquipmentTypeAdapter(Context context, PinnedHeaderExpandableListView expandableListView, int groupLayout, int childLayout) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.mExpandListView = expandableListView;
    }

    public void setDataList(List<ChooseRoomOrType> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (dataList.get(groupPosition).getChooseRoomOrTypeList() == null) {
            return 0;
        }
        return dataList.get(groupPosition).getChooseRoomOrTypeList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).getChooseRoomOrTypeList().get(childPosition);
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
            holder.mGroup = convertView.findViewById(R.id.layout);
            holder.mName = convertView.findViewById(R.id.equipmentName);
            holder.mDeleteBtn = convertView.findViewById(R.id.deleteBtn);
            holder.mCheckBox = convertView.findViewById(R.id.checkbox);
            holder.mImageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        ChooseRoomOrType data = (ChooseRoomOrType) getGroup(groupPosition);
        if (data != null) {
            holder.mName.setText(dataList.get(groupPosition).getName());
            if (data.isSelect()) {
                holder.mCheckBox.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_select));
            } else {
                holder.mCheckBox.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_normal));
            }
            holder.mCheckBox.setTag(R.id.tag_group_position, data.getId());
            if (data.getChooseRoomOrTypeList() == null || data.getChooseRoomOrTypeList().isEmpty()) {
                holder.mDeleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.mDeleteBtn.setVisibility(View.GONE);
            }
        }
        holder.mCheckBox.setOnClickListener(v -> {
            long id = (long) v.getTag(R.id.tag_group_position);
            for (ChooseRoomOrType type : dataList) {
                type.setSelect(type.getId() == id);
                if (type.getChooseRoomOrTypeList() != null) {
                    for (ChooseRoomOrType type2 : type.getChooseRoomOrTypeList()) {
                        type2.setSelect(type2.getId() == id);
                    }
                }
            }
            notifyDataSetChanged();
        });

        if (isExpanded) {
            holder.mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow_open));
        } else {
            holder.mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_employee_arrow));
        }
        holder.mDeleteBtn.setTag(groupPosition);
        holder.mDeleteBtn.setOnClickListener(v -> {
            int position = (int) v.getTag();
            delete(dataList.get(position));
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder vHolder;
        ChooseRoomOrType data = (ChooseRoomOrType) getChild(groupPosition, childPosition);
        if (convertView == null) {
            vHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(childLayout, null);
            vHolder.mGroup = convertView.findViewById(R.id.layout);
            vHolder.mName = convertView.findViewById(R.id.equipmentTypeName);
            vHolder.mCheckBox = convertView.findViewById(R.id.checkbox);
            vHolder.mLine = convertView.findViewById(R.id.divisionView);
            vHolder.mDeleteBtn = convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ChildViewHolder) convertView.getTag();
        }
        if (data != null) {
            vHolder.mName.setText(data.getName());
            if (data.isSelect()) {
                vHolder.mCheckBox.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_select));
            } else {
                vHolder.mCheckBox.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_normal));
            }
            vHolder.mCheckBox.setTag(R.id.tag_child_position, data.getId());
        }
        vHolder.mCheckBox.setOnClickListener(v -> {
            long id = (long) v.getTag(R.id.tag_child_position);
            for (ChooseRoomOrType type : dataList) {
                type.setSelect(type.getId() == id);
                if (type.getChooseRoomOrTypeList() != null) {
                    for (ChooseRoomOrType type2 : type.getChooseRoomOrTypeList()) {
                        type2.setSelect(type2.getId() == id);
                    }
                }
            }
            notifyDataSetChanged();
        });
        vHolder.mDeleteBtn.setTag(R.id.tag_group_position,groupPosition);
        vHolder.mDeleteBtn.setTag(R.id.tag_child_position,childPosition);
        vHolder.mDeleteBtn.setOnClickListener(v -> {
            int groupP = (int) v.getTag(R.id.tag_group_position);
            int childP = (int) v.getTag(R.id.tag_child_position);
            delete(dataList.get(groupP).getChooseRoomOrTypeList().get(childP));
        });
        return convertView;
    }

    private void delete(ChooseRoomOrType chooseRoomOrType) {
        if (deleteCallback != null) {
            deleteCallback.onDelete(chooseRoomOrType);
        }
    }

    public void setDeleteCallback(DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }

    private DeleteCallback deleteCallback;

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface DeleteCallback {
        void onDelete(ChooseRoomOrType chooseRoomOrType);
    }

    /**
     * 外部显示ViewHolder
     */
    private static class GroupViewHolder {
        LinearLayout mGroup;
        TextView mName;
        ImageView mCheckBox;
        Button mDeleteBtn;
        ImageView mImageView;
    }

    /**
     * 内部显示ViewHolder
     */
    private static class ChildViewHolder {
        LinearLayout mGroup;
        TextView mName;
        ImageView mCheckBox;
        View mLine;
        Button mDeleteBtn;
    }
}
