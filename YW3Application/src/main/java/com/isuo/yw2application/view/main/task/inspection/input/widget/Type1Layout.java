package com.isuo.yw2application.view.main.task.inspection.input.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.ShareDataDb;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.EquipmentBean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 录入数据 定性类型
 * Created by zhangan on 2018/2/27.
 */

public class Type1Layout extends LinearLayout {
    private Context mContext;
    private boolean canEdit;
    private DataItemBean dataItemBean;
    private EquipmentBean equipmentBean;
    private TextView tv_check_result;
    private TextView tv_content_type_1;
    private ImageView iv_list_arrow;

    public Type1Layout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.layout_input_type_1, this);
    }

    public void setDataToView(boolean canEdit, DataItemValueListBean bean, EquipmentBean equipmentBean) {
        this.canEdit = canEdit;
        this.dataItemBean = bean.getDataItem();
        this.equipmentBean = equipmentBean;
        TextView tv_value_title = findViewById(R.id.tv_value_title);
        tv_check_result = findViewById(R.id.tv_check_result);
        tv_content_type_1 = findViewById(R.id.tv_content_type_1);
        TextView tv_last_time = findViewById(R.id.tv_last_time);
        iv_list_arrow = findViewById(R.id.iv_list_arrow);

        if (!TextUtils.isEmpty(dataItemBean.getInspectionName())) {
            tv_value_title.setText(dataItemBean.getInspectionName());
        }
        if (!TextUtils.isEmpty(dataItemBean.getChooseInspectionName())) {
            tv_check_result.setText(dataItemBean.getChooseInspectionName());
            tv_check_result.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            tv_content_type_1.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
            iv_list_arrow.setVisibility(View.GONE);
        } else {
            tv_check_result.setText("");
            tv_check_result.setTextColor(mContext.getResources().getColor(R.color.colorAAB5C9));
            tv_content_type_1.setTextColor(mContext.getResources().getColor(R.color.colorAAB5C9));
            iv_list_arrow.setVisibility(View.VISIBLE);
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
        findViewById(R.id.ll_check_result).setOnClickListener(onType1ClickListener);
    }

    private final OnClickListener onType1ClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!canEdit) {
                return;
            }
            if (dataItemBean == null) {
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
                            if (TextUtils.isEmpty(dataItemBean.getValue()) || !dataItemBean.getValue()
                                    .equals(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()))) {
                                dataItemBean.setValue(String.valueOf(dataItemBean.getInspectionItemOptionList().get(position).getId()));
                                dataItemBean.setChooseInspectionName(dataItemBean.getInspectionItemOptionList().get(position).getOptionName());
                                EquipmentDataDb dataDb = dataItemBean.getEquipmentDataDb();
                                dataDb.setChooseInspectionName(text.toString());
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
                                tv_check_result.setText(dataItemBean.getChooseInspectionName());
                                tv_check_result.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                                tv_content_type_1.setTextColor(mContext.getResources().getColor(R.color.color4C8FE2));
                                iv_list_arrow.setVisibility(View.GONE);
                            }
                        }
                    })
                    .show();
        }
    };

}
