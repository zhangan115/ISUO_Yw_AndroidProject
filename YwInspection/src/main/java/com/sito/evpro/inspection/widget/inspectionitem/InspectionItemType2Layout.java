package com.sito.evpro.inspection.widget.inspectionitem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;

/**
 * Created by zhangan on 2017-07-10.
 */

public class InspectionItemType2Layout extends LinearLayout {
    private Context mContext;
    private DataItemBean mDataItemBean;
    private EditText et_value;
    private TextView tv_norma_value, tv_last_value, iv_value;
    private boolean canEdit;
    private OnDataChangeListener onDataChangeListener;
    private int position;
    private String enterStr, olderStr;
    private boolean isFocus;

    public void setOnDataChangeListener(int position, OnDataChangeListener changeListener) {
        this.position = position;
        this.onDataChangeListener = changeListener;
    }

    public InspectionItemType2Layout(Context context) {
        super(context);
        init(context);
    }

    public InspectionItemType2Layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.layout_inspection_item_type_2, this);
        et_value = (EditText) findViewById(R.id.et_value);
        tv_norma_value = (TextView) findViewById(R.id.tv_norma_value);
        iv_value = (TextView) findViewById(R.id.iv_value);
        tv_last_value = (TextView) findViewById(R.id.tv_last_value);
        et_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enterStr = s.toString();
                if (!enterStr.equals(mDataItemBean.getValue())) {
                    mDataItemBean.setValue(enterStr);
                    mDataItemBean.getEquipmentDataDb().setValue(enterStr);
                    if (mEquipmentDb.getUploadState()) {
                        mEquipmentDb.setUploadState(false);
                    }
                    if (onDataChangeListener != null) {
                        onDataChangeListener.onDataChange();
                    }
                }
            }
        });
    }

    private EquipmentDb mEquipmentDb;


    public void setCanEdit(boolean edit) {
        if (edit) {
            et_value.setVisibility(View.VISIBLE);
            iv_value.setVisibility(View.GONE);
        } else {
            et_value.setVisibility(View.GONE);
            iv_value.setVisibility(View.VISIBLE);
        }
    }
}
