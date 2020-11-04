package com.isuo.yw2application.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.ShareDataDb;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.EquipmentBean;
import com.sito.library.utils.DisplayUtil;

import java.text.MessageFormat;

/**
 * 录入数据 定性类型
 * Created by zhangan on 2018/2/27.
 */

public class Type1LayoutNew extends LinearLayout {
    private Context mContext;
    private boolean canEdit;
    private DataItemBean dataItemBean;
    private EquipmentBean equipmentBean;
    private LinearLayout resultLayout;

    public Type1LayoutNew(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.layout_input_type_1_new, this);
    }

    public void setDataToView(boolean canEdit, DataItemValueListBean bean, EquipmentBean equipmentBean) {
        this.canEdit = canEdit;
        if (canEdit) {
            long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();
            if (!TextUtils.isEmpty(bean.getValue())) {
                if (currentUserId != bean.getUserId()) {
                    this.canEdit = false;
                }
            }
        }
        this.dataItemBean = bean.getDataItem();
        this.equipmentBean = equipmentBean;
        TextView tv_value_title = findViewById(R.id.tv_value_title);
        TextView tv_last_time = findViewById(R.id.tv_last_time);

        if (!TextUtils.isEmpty(dataItemBean.getInspectionName())) {
            tv_value_title.setText(dataItemBean.getInspectionName());
        }
        String lastValueStr = null;
        if (!TextUtils.isEmpty(bean.getLastValue()) && dataItemBean.getInspectionItemOptionList() != null
                && dataItemBean.getInspectionItemOptionList().size() > 0) {
            for (int i = 0; i < dataItemBean.getInspectionItemOptionList().size(); i++) {
                if (bean.getLastValue().equals(String.valueOf(dataItemBean.getInspectionItemOptionList().get(i).getId()))) {
                    lastValueStr = dataItemBean.getInspectionItemOptionList().get(i).getOptionName();
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(lastValueStr)) {
            tv_last_time.setText(MessageFormat.format("上次记录:{0}", lastValueStr));
        } else {
            tv_last_time.setText("上次记录:无");
        }
        if (dataItemBean == null) {
            return;
        }
        if (dataItemBean.getInspectionItemOptionList() == null || dataItemBean.getInspectionItemOptionList().size() == 0) {
            return;
        }
        resultLayout = findViewById(R.id.chooseResultLayout);
        for (int i = 0; i < dataItemBean.getInspectionItemOptionList().size(); i++) {
            String name = dataItemBean.getInspectionItemOptionList().get(i).getOptionName();
            TextView button = new TextView(getContext());
            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, DisplayUtil.dip2px(getContext(), 10), 0, 0);
            button.setLayoutParams(params);
            button.setTag(i);
            button.setPadding(0, DisplayUtil.dip2px(getContext(), 8), 0, DisplayUtil.dip2px(getContext(), 8));
            button.setBackgroundColor(getContext().getResources().getColor(R.color.colorWhite));
            button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            button.setTextColor(getContext().getResources().getColor(R.color.gray_999999));
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            button.setTag(R.id.tag_object, dataItemBean.getInspectionItemOptionList().get(i).getId());
            button.setText(name);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    if (view instanceof TextView) {
                        String text = (((TextView) view).getText()).toString();
                        selectChange(position, text);
                    }
                }
            });
            resultLayout.addView(button);
        }
        updateButtonState();
    }

    private void updateButtonState() {
        if (resultLayout != null) {
            String value = dataItemBean.getEquipmentDataDb().getValue();
            for (int i = 0; i < resultLayout.getChildCount(); i++) {
                TextView button = (TextView) resultLayout.getChildAt(i);
                long id = (long) button.getTag(R.id.tag_object);
                String idStringValue = String.valueOf(id);
                boolean isSelect = !TextUtils.isEmpty(value) && TextUtils.equals(idStringValue, value);
                button.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(), 5));
                Drawable draSelect = getResources().getDrawable(R.drawable.choose_select);
                draSelect.setBounds(0, 0, draSelect.getMinimumWidth(), draSelect.getMinimumHeight());
                Drawable draUnSelect = getResources().getDrawable(R.drawable.choose_normal);
                draUnSelect.setBounds(0, 0, draUnSelect.getMinimumWidth(), draUnSelect.getMinimumHeight());
                if (isSelect) {
                    button.setCompoundDrawables(draSelect, null, null, null);
                } else {
                    button.setCompoundDrawables(draUnSelect, null, null, null);
                }
            }
        }
    }

    private void selectChange(int position, String text) {
        if (TextUtils.isEmpty(dataItemBean.getValue()) || !dataItemBean.getValue()
                .equals(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()))) {
            dataItemBean.setValue(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()));
            dataItemBean.setChooseInspectionName(dataItemBean.getInspectionItemOptionList().get(position).getOptionName());
            EquipmentDataDb dataDb = dataItemBean.getEquipmentDataDb();
            dataDb.setChooseInspectionName(text);
            dataDb.setValue(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()));
            if (equipmentBean.getEquipmentDb().getUploadState()) {
                equipmentBean.getEquipmentDb().setUploadState(false);
            }
            if (equipmentBean.getEquipmentDb().getCanUpload()) {
                equipmentBean.getEquipmentDb().setCanUpload(false);
            }
            if (dataItemBean.getIsShareValue() == 1) {
                //保存共享的数据
                ShareDataDb shareDataDb = ShareDataDb.getShareDataDb(dataItemBean.getEquipmentDataDb());
                Yw2Application.getInstance().getDaoSession().getShareDataDbDao()
                        .insertOrReplace(shareDataDb);
            }
            Yw2Application.getInstance().getDaoSession().getEquipmentDbDao()
                    .insertOrReplace(equipmentBean.getEquipmentDb());

            Yw2Application.getInstance().getDaoSession()
                    .getEquipmentDataDbDao().insertOrReplaceInTx(dataDb);
            updateButtonState();
        }
    }
}
