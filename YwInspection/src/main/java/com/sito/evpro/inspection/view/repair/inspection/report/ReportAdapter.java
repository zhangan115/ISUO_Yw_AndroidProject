package com.sito.evpro.inspection.view.repair.inspection.report;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.view.photo.ViewPagePhotoActivity;
import com.sito.evpro.inspection.widget.inspectionitem.OnDataChangeListener;
import com.sito.library.utils.GlideUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡检录入界面数据adapter
 * Created by zhangan on 2017-07-27.
 */
class ReportAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<TaskEquipmentBean> dataList;
    private boolean canEdit;

    ReportAdapter(Context mContext, List<TaskEquipmentBean> dataList, boolean canEdit) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.canEdit = canEdit;
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(groupPosition).getDataList().get(0).getDataItemValueList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_report_group, parent, false);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_equip_name = (TextView) convertView.findViewById(R.id.tv_equip_name);
            holder.iv_alarm_state = (ImageView) convertView.findViewById(R.id.iv_alarm_state);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (dataList.get(groupPosition) != null) {
            holder.tv_position.setText(String.valueOf(groupPosition + 1));
            if (TextUtils.isEmpty(dataList.get(groupPosition).getEquipment().getEquipmentSn())) {
                holder.tv_equip_name.setText(dataList.get(groupPosition).getEquipment().getEquipmentName());
            } else {
                holder.tv_equip_name.setText(dataList.get(groupPosition).getEquipment().getEquipmentName()
                        + "(" + dataList.get(groupPosition).getEquipment().getEquipmentSn() + ")");
            }
            if (dataList.get(groupPosition).getEquipment().getEquipmentDb().getAlarmState()) {
                holder.iv_alarm_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fault_call_icon_selected));
            } else {
                holder.iv_alarm_state.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_inspection_item_alarm));
            }
        }
        holder.iv_alarm_state.setTag(R.id.tag_position, groupPosition);
        holder.iv_alarm_state.setOnClickListener(alarmClick);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DataItemBean dataItemBean = dataList.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition).getDataItem();
        final String lastValue = dataList.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition).getLastValue();
        View currentFocus = ((Activity) mContext).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_report, parent, false);
            viewHolder = new ChildViewHolder();
            viewHolder.ll_check_result = (LinearLayout) convertView.findViewById(R.id.ll_check_result);
            viewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            viewHolder.tv_check_result = (TextView) convertView.findViewById(R.id.tv_check_result);
            viewHolder.tv_last_time = (TextView) convertView.findViewById(R.id.tv_last_time);
            viewHolder.tv_value_title = (TextView) convertView.findViewById(R.id.tv_value_title);
            viewHolder.ll_type_1 = (LinearLayout) convertView.findViewById(R.id.ll_type_1);
            viewHolder.tv_content_type_1 = (TextView) convertView.findViewById(R.id.tv_content_type_1);
            viewHolder.iv_list_arrow = (ImageView) convertView.findViewById(R.id.iv_list_arrow);

            viewHolder.et_value = (EditText) convertView.findViewById(R.id.et_value);
            viewHolder.iv_value = (TextView) convertView.findViewById(R.id.iv_value);
            viewHolder.tv_last_value = (TextView) convertView.findViewById(R.id.tv_last_value);
            viewHolder.tv_norma_value = (TextView) convertView.findViewById(R.id.tv_norma_value);
            viewHolder.ll_type_2 = (LinearLayout) convertView.findViewById(R.id.ll_type_2);
            viewHolder.tv_value_title_2 = (TextView) convertView.findViewById(R.id.tv_value_title_2);

            viewHolder.iv_take_photo = convertView.findViewById(R.id.iv_take_photo);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            viewHolder.ll_type_3 = convertView.findViewById(R.id.ll_type_3);
            viewHolder.progressBar = convertView.findViewById(R.id.progressBar);

            viewHolder.iv_division_1 = convertView.findViewById(R.id.iv_division_1);
            viewHolder.iv_division_2 = convertView.findViewById(R.id.iv_division_2);
            viewHolder.iv_division_3 = convertView.findViewById(R.id.iv_division_3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (getChildrenCount(groupPosition) == 1) {
            viewHolder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg));
        } else if (childPosition == 0) {
            viewHolder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg_up));
        } else if (childPosition == getChildrenCount(groupPosition) - 1) {
            viewHolder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg_down));
        } else {
            viewHolder.ll_item.setBackground(mContext.getResources().getDrawable(R.drawable.bg_centre));
        }
        viewHolder.et_value.setTag(R.id.tag_position, groupPosition);
        viewHolder.et_value.setTag(R.id.tag_position_1, childPosition);
        int type = dataList.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition).getDataItem().getInspectionType();
        if (type == ConstantInt.DATA_VALUE_TYPE_1) {
            viewHolder.ll_type_1.setVisibility(View.VISIBLE);
            viewHolder.ll_type_2.setVisibility(View.GONE);
            viewHolder.ll_type_3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(dataItemBean.getInspectionName())) {
                viewHolder.tv_value_title.setText(dataItemBean.getInspectionName());
            }
            if (!TextUtils.isEmpty(dataItemBean.getChooseInspectionName())) {
                viewHolder.tv_check_result.setText(dataItemBean.getChooseInspectionName());
                viewHolder.tv_check_result.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                viewHolder.tv_content_type_1.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                viewHolder.iv_list_arrow.setVisibility(View.GONE);
            } else {
                viewHolder.tv_check_result.setText("");
                viewHolder.tv_check_result.setTextColor(mContext.getResources().getColor(R.color.colorAAB5C9));
                viewHolder.tv_content_type_1.setTextColor(mContext.getResources().getColor(R.color.colorAAB5C9));
                viewHolder.iv_list_arrow.setVisibility(View.VISIBLE);
            }
            String lastValueStr = null;
            if (!TextUtils.isEmpty(lastValue) && dataItemBean.getInspectionItemOptionList() != null
                    && dataItemBean.getInspectionItemOptionList().size() > 0) {
                for (int i = 0; i < dataItemBean.getInspectionItemOptionList().size(); i++) {
                    if (lastValue.equals(String.valueOf(dataItemBean.getInspectionItemOptionList().get(i).getId()))) {
                        lastValueStr = dataItemBean.getInspectionItemOptionList().get(i).getOptionName();
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(lastValueStr)) {
                viewHolder.tv_last_time.setText(MessageFormat.format("上次记录:{0}", lastValueStr));
            } else {
                viewHolder.tv_last_time.setText("上次记录:无");
            }
            viewHolder.ll_check_result.setTag(R.id.tag_object, dataItemBean);
            viewHolder.ll_check_result.setTag(R.id.tag_object_1, dataList.get(groupPosition).getEquipment().getEquipmentDb());
            viewHolder.ll_check_result.setTag(R.id.tag_object_2, viewHolder.tv_check_result);
            viewHolder.ll_check_result.setOnClickListener(onType1ClickListener);

        }
        if (type == ConstantInt.DATA_VALUE_TYPE_2) {
            viewHolder.ll_type_1.setVisibility(View.GONE);
            viewHolder.ll_type_2.setVisibility(View.VISIBLE);
            viewHolder.ll_type_3.setVisibility(View.GONE);
            viewHolder.et_value.addTextChangedListener(new MyTextWatcher(viewHolder, dataList, viewHolder.et_value, type));
            if (canEdit) {
                viewHolder.et_value.setVisibility(View.VISIBLE);
                viewHolder.iv_value.setVisibility(View.GONE);
            } else {
                viewHolder.et_value.setVisibility(View.GONE);
                viewHolder.iv_value.setVisibility(View.VISIBLE);
            }
            if (isNormal(dataItemBean)) {
                viewHolder.et_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                viewHolder.iv_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            } else {
                viewHolder.et_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
                viewHolder.iv_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
            }
            if (TextUtils.isEmpty(dataItemBean.getQuantityUnit())) {
                viewHolder.tv_value_title_2.setText(dataItemBean.getInspectionName());
            } else {
                viewHolder.tv_value_title_2.setText(dataItemBean.getInspectionName() + "(" + dataItemBean.getQuantityUnit() + ")");
            }
            if (TextUtils.isEmpty(dataItemBean.getValue())) {
                viewHolder.et_value.getEditableText().clear();
                viewHolder.et_value.setHint("请输入" + dataItemBean.getInspectionName());
            } else {
                viewHolder.et_value.setText(dataItemBean.getValue());
            }
            viewHolder.iv_value.setText(dataItemBean.getValue());
            String lowerStr = "";
            if (!TextUtils.isEmpty(dataItemBean.getQuantityLowlimit())) {
                lowerStr = dataItemBean.getQuantityLowlimit();
            }
            String upperStr = "";
            if (!TextUtils.isEmpty(dataItemBean.getQuantityUplimit())) {
                upperStr = dataItemBean.getQuantityUplimit();
            }
            if (!TextUtils.isEmpty(lastValue)) {
                viewHolder.tv_last_value.setText(MessageFormat.format("上次记录:{0}", lastValue));
            } else {
                viewHolder.tv_last_value.setText("上次记录:无");
            }
            if (!TextUtils.isEmpty(lowerStr) || !TextUtils.isEmpty(upperStr)) {
                viewHolder.tv_norma_value.setVisibility(View.VISIBLE);
                viewHolder.tv_norma_value.setText("正常范围 [" + lowerStr + "~" + upperStr + " ]");
            } else {
                viewHolder.tv_norma_value.setVisibility(View.GONE);
            }
        }
        if (type == ConstantInt.DATA_VALUE_TYPE_4) {
            viewHolder.ll_type_1.setVisibility(View.GONE);
            viewHolder.ll_type_2.setVisibility(View.VISIBLE);
            viewHolder.ll_type_3.setVisibility(View.GONE);
            viewHolder.et_value.addTextChangedListener(new MyTextWatcher(viewHolder, dataList, viewHolder.et_value, type));
            if (canEdit) {
                viewHolder.et_value.setVisibility(View.VISIBLE);
                viewHolder.iv_value.setVisibility(View.GONE);
            } else {
                viewHolder.et_value.setVisibility(View.GONE);
                viewHolder.iv_value.setVisibility(View.VISIBLE);
            }
            viewHolder.et_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            viewHolder.iv_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            if (TextUtils.isEmpty(dataItemBean.getQuantityUnit())) {
                viewHolder.tv_value_title_2.setText(dataItemBean.getInspectionName());
            } else {
                viewHolder.tv_value_title_2.setText(dataItemBean.getInspectionName() + "(" + dataItemBean.getQuantityUnit() + ")");
            }
            if (TextUtils.isEmpty(dataItemBean.getValue())) {
                viewHolder.et_value.getEditableText().clear();
                viewHolder.et_value.setHint("请输入" + dataItemBean.getInspectionName());
            } else {
                viewHolder.et_value.setText(dataItemBean.getValue());
            }
            viewHolder.iv_value.setText(dataItemBean.getValue());
            String lowerStr = "";
            if (!TextUtils.isEmpty(dataItemBean.getQuantityLowlimit())) {
                lowerStr = dataItemBean.getQuantityLowlimit();
            }
            String upperStr = "";
            if (!TextUtils.isEmpty(dataItemBean.getQuantityUplimit())) {
                upperStr = dataItemBean.getQuantityUplimit();
            }
            if (!TextUtils.isEmpty(lastValue)) {
                viewHolder.tv_last_value.setText(MessageFormat.format("上次记录:{0}", lastValue));
            } else {
                viewHolder.tv_last_value.setText("上次记录:无");
            }
            if (!TextUtils.isEmpty(lowerStr) || !TextUtils.isEmpty(upperStr)) {
                viewHolder.tv_norma_value.setVisibility(View.VISIBLE);
                viewHolder.tv_norma_value.setText("正常范围 [" + lowerStr + "~" + upperStr + " ]");
            } else {
                viewHolder.tv_norma_value.setVisibility(View.GONE);
            }
        }
        if (type == ConstantInt.DATA_VALUE_TYPE_3) {
            viewHolder.ll_type_1.setVisibility(View.GONE);
            viewHolder.ll_type_2.setVisibility(View.GONE);
            viewHolder.ll_type_3.setVisibility(View.VISIBLE);
            viewHolder.tv_title.setText(dataItemBean.getInspectionName());
            if (dataItemBean.getEquipmentDataDb() != null && TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                dataItemBean.setLocalFile(dataItemBean.getEquipmentDataDb().getLocalPhoto());
            }
            if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                viewHolder.iv_take_photo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo_button));
            } else {
                GlideUtils.ShowImage(mContext, dataItemBean.getLocalFile(), viewHolder.iv_take_photo, R.drawable.picture_default);
            }
            if (dataItemBean.isUploading()) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            viewHolder.iv_take_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                        ViewPagePhotoActivity.startActivity(mContext, new String[]{dataItemBean.getLocalFile()}, 0);
                        return;
                    }
                    if (!canEdit) {
                        return;
                    }
                    if (onTakePhotoListener != null) {
                        onTakePhotoListener.onTakePhoto(groupPosition, childPosition);
                    }
                }
            });
            viewHolder.iv_take_photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (TextUtils.isEmpty(dataItemBean.getLocalFile())) {
                        return false;
                    }
                    new MaterialDialog.Builder(mContext)
                            .items(R.array.choose_condition_2)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    if (onTakePhotoListener == null) {
                                        return;
                                    }
                                    if (!canEdit) {
                                        return;
                                    }
                                    switch (position) {
                                        case 0://删除照片
                                            onTakePhotoListener.onDeletePhoto(groupPosition, childPosition);
                                            break;
                                        default://重新拍照
                                            onTakePhotoListener.onAgainPhoto(groupPosition, childPosition);
                                            break;
                                    }
                                }
                            })
                            .show();
                    return true;
                }
            });
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
        TextView tv_equip_name;
        TextView tv_position;
        ImageView iv_alarm_state;
    }

    private class ChildViewHolder {
        LinearLayout ll_item;

        TextView tv_value_title;
        TextView tv_content_type_1;
        ImageView iv_list_arrow;
        TextView tv_check_result;
        TextView tv_last_time;
        LinearLayout ll_check_result;
        LinearLayout ll_type_1;

        EditText et_value;
        TextView iv_value;
        TextView tv_last_value;
        TextView tv_norma_value;
        TextView tv_value_title_2;
        LinearLayout ll_type_2;

        TextView tv_title;
        ImageView iv_take_photo;
        LinearLayout ll_type_3;
        ProgressBar progressBar;

        View iv_division_1;
        View iv_division_2;
        View iv_division_3;
    }

    private View.OnClickListener onType1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final DataItemBean dataItemBean = (DataItemBean) v.getTag(R.id.tag_object);
            final EquipmentDb equipmentDb = (EquipmentDb) v.getTag(R.id.tag_object_1);
            final TextView textView = (TextView) v.getTag(R.id.tag_object_2);
            if (!canEdit) {
                return;
            }
            if (dataItemBean == null) {
                return;
            }
            if (!(mContext instanceof Activity)) {
                return;
            }
            if (dataItemBean.getInspectionItemOptionList() == null || dataItemBean.getInspectionItemOptionList().size() == 0) {
                return;
            }
            List<String> items = new ArrayList<>();
            for (int i = 0; i < dataItemBean.getInspectionItemOptionList().size(); i++) {
                items.add(dataItemBean.getInspectionItemOptionList().get(i).getOptionName());
            }
            new MaterialDialog.Builder(mContext)
                    .items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            if (TextUtils.isEmpty(dataItemBean.getValue()) ||
                                    !dataItemBean.getValue().equals(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()))) {
                                textView.setText(text);
                                dataItemBean.setValue(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()));
                                dataItemBean.setChooseInspectionName(dataItemBean.getInspectionItemOptionList().get(position).getOptionName());
                                EquipmentDataDb dataDb = dataItemBean.getEquipmentDataDb();
                                dataDb.setChooseInspectionName(text.toString());
                                dataDb.setValue(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()));
                                if (equipmentDb.getUploadState()) {
                                    equipmentDb.setUploadState(false);
                                    InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
                                            .insertOrReplace(equipmentDb);
                                }
                                InspectionApp.getInstance().getDaoSession()
                                        .getEquipmentDataDbDao().insertOrReplaceInTx(dataDb);
                                notifyDataSetChanged();
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChange();
                                }
                            }
                        }
                    })
                    .show();
        }
    };

    interface OnTakePhotoListener {

        void onTakePhoto(int groupPosition, int childPosition);

        void onDeletePhoto(int groupPosition, int childPosition);

        void onAgainPhoto(int groupPosition, int childPosition);
    }

    interface OnAlarmListener {

        void onAlarm(int position);
    }

    private OnAlarmListener onAlarmListener;
    private OnTakePhotoListener onTakePhotoListener;
    private OnDataChangeListener dataChangeListener;

    void setOnAlarmListener(OnAlarmListener onAlarmListener) {
        this.onAlarmListener = onAlarmListener;
    }

    void setOnTakePhotoListener(OnTakePhotoListener onTakePhotoListener) {
        this.onTakePhotoListener = onTakePhotoListener;
    }

    void setDataChangeListener(OnDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    private View.OnClickListener alarmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_position);
            if (onAlarmListener == null) {
                return;
            }
            if (!canEdit) {
                return;
            }
            onAlarmListener.onAlarm(position);
        }
    };

    private class MyTextWatcher implements TextWatcher {

        private ChildViewHolder holder;
        private List<TaskEquipmentBean> taskEquipmentBeen;
        private EditText editText;


        MyTextWatcher(ChildViewHolder holder, List<TaskEquipmentBean> contents, EditText editText, int type) {
            this.holder = holder;
            this.taskEquipmentBeen = contents;
            this.editText = editText;
            if (type == ConstantInt.DATA_VALUE_TYPE_2) {
                this.editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                this.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                this.editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                this.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int groupPosition = (int) holder.et_value.getTag(R.id.tag_position);
            int childPosition = (int) holder.et_value.getTag(R.id.tag_position_1);
            if (s.length() == 0) {
                return;
            }
            if (taskEquipmentBeen.get(groupPosition).getDataList().get(0).getDataItemValueList()
                    .get(childPosition)
                    .getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_4) {
                return;
            }
            String older = s.subSequence(0, start).toString();
            String enter = s.subSequence(start, s.length()).toString();
            boolean isWatcher = enter.matches("[0-9.-]+");
            boolean hasPoint = older.contains(".") && enter.contains(".");
            boolean startPoint = s.toString().startsWith(".");
            boolean hasMinus;
            hasMinus = older.contains("-") && enter.contains("-")
                    || older.length() > 0 && enter.contains("-");
            if (startPoint) {
                editText.setText("");
                if (editText.getText().length() == 0)
                    return;
                return;
            }
            if (!isWatcher || hasPoint || hasMinus) {
                editText.setText(older);
                if (editText.getText().length() == 0) {
                    return;
                }
                editText.setSelection(older.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (holder != null && taskEquipmentBeen != null) {
                int groupPosition = (int) holder.et_value.getTag(R.id.tag_position);
                int childPosition = (int) holder.et_value.getTag(R.id.tag_position_1);
                if (TextUtils.isEmpty(taskEquipmentBeen.get(groupPosition).getDataList().get(0).getDataItemValueList()
                        .get(childPosition)
                        .getDataItem().getValue()) || !taskEquipmentBeen.get(groupPosition)
                        .getDataList().get(0).getDataItemValueList().get(childPosition)
                        .getDataItem().getValue().equals(editText.getText().toString())) {
                    taskEquipmentBeen.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition)
                            .getDataItem().setValue(editText.getText().toString());
                    taskEquipmentBeen.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition)
                            .getDataItem().getEquipmentDataDb().setValue(editText.getText().toString());
                    if (taskEquipmentBeen.get(groupPosition).getEquipment().getEquipmentDb().getUploadState()) {
                        taskEquipmentBeen.get(groupPosition).getEquipment().getEquipmentDb().setUploadState(false);
                    }
                    if (taskEquipmentBeen.get(groupPosition).getDataList().get(0).getDataItemValueList().get(childPosition)
                            .getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2) {
                        if (isNormal(taskEquipmentBeen.get(groupPosition).getDataList()
                                .get(0).getDataItemValueList().get(childPosition)
                                .getDataItem())) {
                            holder.et_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                        } else {
                            holder.et_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
                        }
                    }
                    if (dataChangeListener != null) {
                        dataChangeListener.onDataChange();
                    }
                }
            }
        }
    }

    private boolean isNormal(DataItemBean dataItemBean) {
        if (dataItemBean.getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2) {
            try {
                String lower = dataItemBean.getQuantityLowlimit();
                String upper = dataItemBean.getQuantityUplimit();
                if (TextUtils.isEmpty(lower) && TextUtils.isEmpty(upper)) {
                    return true;
                } else if (TextUtils.isEmpty(lower) && !TextUtils.isEmpty(upper)) {
                    float upperValue = Float.valueOf(upper);
                    float value = Float.valueOf(dataItemBean.getValue());
                    return TextUtils.isEmpty(dataItemBean.getValue()) || value <= upperValue;
                } else if (!TextUtils.isEmpty(lower) && TextUtils.isEmpty(upper)) {
                    float lowerValue = Float.valueOf(lower);
                    float value = Float.valueOf(dataItemBean.getValue());
                    return TextUtils.isEmpty(dataItemBean.getValue()) || value >= lowerValue;
                } else {
                    float lowerValue = Float.valueOf(lower);
                    float upperValue = Float.valueOf(upper);
                    float value = Float.valueOf(dataItemBean.getValue());
                    return TextUtils.isEmpty(dataItemBean.getValue()) || (lowerValue <= value) && (value <= upperValue);
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
