package com.isuo.yw2application.view.main.task.inspection.input.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.EquipmentBean;

import java.text.MessageFormat;

/**
 * 入数据 定量类型，文本类型
 * Created by zhangan on 2018/2/27.
 */

public class Type2_4Layout extends LinearLayout {

    private Context mContext;
    private EditText et_value;
    private DataItemBean dataItemBean;
    private EquipmentBean equipmentBean;

    public Type2_4Layout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.layout_input_type_2_4, this);
        et_value = findViewById(R.id.et_value);
    }

    public void setDataToView(boolean canEdit, DataItemValueListBean bean, EquipmentBean equipmentBean) {
        this.dataItemBean = bean.getDataItem();
        this.equipmentBean = equipmentBean;
        if (canEdit) {
            long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();
            if (!TextUtils.isEmpty(bean.getValue())) {
                if (currentUserId != bean.getUserId()) {
                    canEdit = false;
                }
            }
        }
        TextView iv_value = findViewById(R.id.iv_value);
        TextView tv_value_title_2 = findViewById(R.id.tv_value_title_2);
        TextView tv_last_value = findViewById(R.id.tv_last_value);
        TextView tv_norma_value = findViewById(R.id.tv_norma_value);
        if (dataItemBean.getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2) {
            et_value.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            et_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else {
            et_value.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            et_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        }
        if (canEdit) {
            et_value.setVisibility(View.VISIBLE);
            iv_value.setVisibility(View.GONE);
        } else {
            et_value.setVisibility(View.GONE);
            iv_value.setVisibility(View.VISIBLE);
        }
        if (isNormal(dataItemBean)) {
            et_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            iv_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
        } else {
            et_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
            iv_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
        }
        if (TextUtils.isEmpty(dataItemBean.getQuantityUnit())) {
            tv_value_title_2.setText(dataItemBean.getInspectionName());
        } else {
            String str = dataItemBean.getInspectionName() + "(" + dataItemBean.getQuantityUnit() + ")";
            tv_value_title_2.setText(str);
        }
        if (TextUtils.isEmpty(dataItemBean.getValue())) {
            et_value.getEditableText().clear();
            et_value.setHint("请输入" + dataItemBean.getInspectionName());
        } else {
            et_value.setText(dataItemBean.getValue());
        }
        iv_value.setText(dataItemBean.getValue());
        String lowerStr = "";
        if (!TextUtils.isEmpty(dataItemBean.getQuantityLowlimit())) {
            lowerStr = dataItemBean.getQuantityLowlimit();
        }
        String upperStr = "";
        if (!TextUtils.isEmpty(dataItemBean.getQuantityUplimit())) {
            upperStr = dataItemBean.getQuantityUplimit();
        }
        if (!TextUtils.isEmpty(bean.getLastValue())) {
            tv_last_value.setText(MessageFormat.format("上次记录:{0}", bean.getLastValue()));
        } else {
            tv_last_value.setText("上次记录:无");
        }
        if (!TextUtils.isEmpty(lowerStr) || !TextUtils.isEmpty(upperStr)) {
            tv_norma_value.setVisibility(View.VISIBLE);
            tv_norma_value.setText("正常范围 [" + lowerStr + "~" + upperStr + " ]");
        } else {
            tv_norma_value.setVisibility(View.GONE);
        }
        et_value.addTextChangedListener(new MyTextWatcher());
    }

    private class MyTextWatcher implements TextWatcher {

        public MyTextWatcher() {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                return;
            }
            if (dataItemBean.getInspectionType() == ConstantInt.DATA_VALUE_TYPE_4) {
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
                et_value.setText("");
                if (et_value.getText().length() == 0)
                    return;
                return;
            }
            if (!isWatcher || hasPoint || hasMinus) {
                et_value.setText(older);
                if (et_value.getText().length() == 0) {
                    return;
                }
                et_value.setSelection(older.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (dataItemBean != null) {
                if (TextUtils.isEmpty(dataItemBean.getValue()) || !dataItemBean.getValue().equals(et_value.getText().toString())) {
                    dataItemBean.setValue(et_value.getText().toString());
                    dataItemBean.getEquipmentDataDb().setValue(et_value.getText().toString());
                    if (equipmentBean.getEquipmentDb().getUploadState()) {
                        equipmentBean.getEquipmentDb().setUploadState(false);
                    }
                    if (equipmentBean.getEquipmentDb().getCanUpload()) {
                        equipmentBean.getEquipmentDb().setCanUpload(false);
                    }
                    if (dataItemBean.getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2) {
                        if (isNormal(dataItemBean)) {
                            et_value.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                        } else {
                            et_value.setTextColor(mContext.getResources().getColor(R.color.line_color_4));
                        }
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
