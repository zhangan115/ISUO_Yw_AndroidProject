package com.sito.customer.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;
import com.sito.customer.mode.inject.bean.InjectEquipment;
import com.sito.library.utils.DataUtil;

import java.text.MessageFormat;


/**
 * 注油管理dialog
 * Created by Administrator on 2017/6/14.
 */

public abstract class InjectionView extends LinearLayout implements View.OnClickListener {

    private int position;
    private TextView editText;
    private InjectEquipment injectEquipment;

    public InjectionView(Context context, InjectEquipment injectEquipment, int position) {
        super(context);
        this.position = position;
        this.injectEquipment = injectEquipment;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_inject, this);
        editText = (TextView) findViewById(R.id.text_cycle);
        TextView img_cancel = (TextView) findViewById(R.id.tv_cancel);
        TextView text_id = (TextView) findViewById(R.id.id_greasing_dialog_id);
        TextView text_lastTime = (TextView) findViewById(R.id.id_greasing_dialog_last_time);
        TextView text_nextTime = (TextView) findViewById(R.id.id_greasing_dialog_next_time);
        if (TextUtils.isEmpty(injectEquipment.getEquipmentSn())) {
            text_id.setText(injectEquipment.getEquipmentName());
        } else {
            text_id.setText(injectEquipment.getEquipmentName() + "\n" + injectEquipment.getEquipmentSn());
        }
        if (injectEquipment.getInjectionOil() != null && injectEquipment.getInjectionOil().getCreateTime() != 0) {
            text_lastTime.setText(DataUtil.timeFormat(injectEquipment.getInjectionOil().getCreateTime(), "yyyy-MM-dd"));
            text_nextTime.setText(DataUtil.timeFormat(injectEquipment.getInjectionOil().getCreateTime()
                    + injectEquipment.getCycle() * 24L * 60L * 60L * 1000L, "yyyy-MM-dd"));
        }
        img_cancel.setOnClickListener(this);
        editText.setText(MessageFormat.format("{0}天", String.valueOf(injectEquipment.getCycle())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                cancel();
                break;
        }
    }

    public abstract void cancel();
}
