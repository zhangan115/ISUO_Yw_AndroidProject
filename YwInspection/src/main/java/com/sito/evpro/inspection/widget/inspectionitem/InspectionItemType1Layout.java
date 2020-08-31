package com.sito.evpro.inspection.widget.inspectionitem;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangan on 2017-07-10.
 */

public class InspectionItemType1Layout extends LinearLayout {

    private Context mContext;
    private DataItemBean mDataItemBean;
    private TextView tv_check_result;
    private TextView tv_value_title;
    private TextView tv_last_time;
    private boolean canEdit;
    private OnDataChangeListener onDataChangeListener;
    private int mPosition;

    public void setOnDataChangeListener(int position, OnDataChangeListener changeListener) {
        this.mPosition = position;
        this.onDataChangeListener = changeListener;
    }

    public InspectionItemType1Layout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.layout_inspection_item_type_1, this);
        tv_value_title = (TextView) findViewById(R.id.tv_value_title);
        tv_check_result = (TextView) findViewById(R.id.tv_check_result);
        tv_last_time = (TextView) findViewById(R.id.tv_last_time);
        findViewById(R.id.ll_check_result).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canEdit) {
                    return;
                }
                if (mDataItemBean == null) {
                    return;
                }
                if (!(mContext instanceof Activity)) {
                    return;
                }
                if (mDataItemBean.getInspectionItemOptionList() == null || mDataItemBean.getInspectionItemOptionList().size() == 0) {
                    return;
                }
                List<String> items = new ArrayList<>();
                for (int i = 0; i < mDataItemBean.getInspectionItemOptionList().size(); i++) {
                    items.add(mDataItemBean.getInspectionItemOptionList().get(i).getOptionName());
                }
                new MaterialDialog.Builder(mContext)
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (TextUtils.isEmpty(mDataItemBean.getValue()) || !mDataItemBean.getValue().equals(String.valueOf(mDataItemBean.getInspectionItemOptionList().get(position).getId()))) {
                                    tv_check_result.setText(text);
                                    mDataItemBean.setValue(String.valueOf(mDataItemBean.getInspectionItemOptionList().get(position).getId()));
                                    mDataItemBean.setChooseInspectionName(mDataItemBean.getInspectionItemOptionList().get(position).getOptionName());
                                    EquipmentDataDb dataDb = mDataItemBean.getEquipmentDataDb();
                                    dataDb.setChooseInspectionName(text.toString());
                                    dataDb.setValue(String.valueOf(mDataItemBean.getInspectionItemOptionList().get(position).getId()));
                                    if (mEquipmentDb.getUploadState()) {
                                        mEquipmentDb.setUploadState(false);
                                        InspectionApp.getInstance().getDaoSession().getEquipmentDbDao()
                                                .insertOrReplace(mEquipmentDb);
                                    }
                                    if (onDataChangeListener != null) {
                                        onDataChangeListener.onDataChange();
                                    }
                                    InspectionApp.getInstance().getDaoSession()
                                            .getEquipmentDataDbDao().insertOrReplaceInTx(dataDb);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    public InspectionItemType1Layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private EquipmentDb mEquipmentDb;

}
