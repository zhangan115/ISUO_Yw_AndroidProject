package com.isuo.yw2application.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.PayMenuBean;

import java.text.MessageFormat;

public class PayContentView extends LinearLayout {

    private TextView[] textViews = new TextView[33];
    public TextView currentTv;

    public PayContentView(Context context) {
        super(context);
        init(context);
    }

    public PayContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setChooseState(boolean isChoose) {
        if (isChoose) {
            payTitleLayout.setBackground(this.context.getResources().getDrawable(R.drawable.green_bg_with_shape));
        } else {
            payTitleLayout.setBackground(this.context.getResources().getDrawable(R.drawable.white_bg_with_shape));
        }
    }

    public OnChooseListener chooseListener;
    LinearLayout payTitleLayout;
    Context context;

    public void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_pay_content, this);
        payTitleLayout = findViewById(R.id.payTitle);
        textViews[0] = findViewById(R.id.text_1);
        textViews[1] = findViewById(R.id.text_2);
        textViews[2] = findViewById(R.id.text_3);
        textViews[3] = findViewById(R.id.text_4);
        textViews[4] = findViewById(R.id.text_5);
        textViews[5] = findViewById(R.id.text_6);
        textViews[6] = findViewById(R.id.text_7);
        textViews[7] = findViewById(R.id.text_8);
        textViews[8] = findViewById(R.id.text_9);
        textViews[9] = findViewById(R.id.text_10);
        textViews[10] = findViewById(R.id.text_11);
        textViews[11] = findViewById(R.id.text_12);
        textViews[12] = findViewById(R.id.text_13);
        textViews[13] = findViewById(R.id.text_14);
        textViews[14] = findViewById(R.id.text_15);
        textViews[15] = findViewById(R.id.text_16);
        textViews[16] = findViewById(R.id.text_17);
        textViews[17] = findViewById(R.id.text_18);
        textViews[18] = findViewById(R.id.text_19);
        textViews[19] = findViewById(R.id.text_20);
        textViews[20] = findViewById(R.id.text_21);
        textViews[21] = findViewById(R.id.text_22);
        textViews[22] = findViewById(R.id.text_23);
        textViews[23] = findViewById(R.id.text_24);
        textViews[24] = findViewById(R.id.text_25);
        textViews[25] = findViewById(R.id.text_26);
        textViews[26] = findViewById(R.id.text_27);
        textViews[27] = findViewById(R.id.text_28);
        textViews[28] = findViewById(R.id.text_29);
        textViews[29] = findViewById(R.id.text_30);
        textViews[30] = findViewById(R.id.text_31);
        textViews[31] = findViewById(R.id.text_32);
        textViews[32] = findViewById(R.id.text_33);
        payTitleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseListener != null && bean != null) {
                    chooseListener.chooseMenu(bean);
                }
            }
        });
        currentTv = findViewById(R.id.checkDetail);
    }

    public interface OnChooseListener {
        void chooseMenu(PayMenuBean bean);
    }

    private PayMenuBean bean = null;

    @SuppressLint("DefaultLocale")
    public void setData(PayMenuBean bean) {
        this.bean = bean;
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);
        text1.setText(bean.getMenuName());
        text2.setText(String.format("%.0f", bean.getPrice()));
        if (bean.getPrice() == 0) {
            text3.setText("元");
        } else {
            text3.setText("元/年");
        }
        textViews[9].setText(MessageFormat.format("{0}G", bean.getWorkSpace() / 1024));
        textViews[10].setText("2年");
        //安全规则   一键报警
        if (bean.getSafetySo() == 0) {
            textViews[11].setText("不开放");
            textViews[12].setText("不开放");
            textViews[11].setTextColor(context.getResources().getColor(R.color.colorF05340));
            textViews[12].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[11].setText("开放");
            textViews[12].setText("开放");
        }
        textViews[13].setText(MessageFormat.format("限制{0}条计划", bean.getPlanCount()));
        if (bean.getPlanEquipmentCount() == 9999) {
            textViews[16].setText("不限制");
        } else {
            textViews[16].setText(MessageFormat.format("不超过{0}台", bean.getPlanEquipmentCount()));
        }
        textViews[17].setText("同巡检条数");
        //指派检修
        textViews[18].setText(MessageFormat.format("限制{0}条/周", bean.getRepairCount()));
        //故障上报、故障闭环管理
        textViews[19].setText(MessageFormat.format("限制{0}条/周", bean.getFaultCount()));
        textViews[20].setText("无限制使用");
        textViews[21].setText(MessageFormat.format("限制台{0}台", bean.getEquipmentCount()));
        if (bean.getSafetySo() == 0) {
            textViews[22].setText("不开放");
            textViews[22].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[22].setText("开放");
        }
        if (bean.getIncrementWorkSo() == 0) {
            textViews[24].setText("不开放");
            textViews[24].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[24].setText("开放");
        }
        if (bean.getOilSo() == 0) {
            textViews[25].setText("不开放");
            textViews[25].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[25].setText("开放");
        }
        if (bean.getToolSo() == 0) {
            textViews[26].setText("不开放");
            textViews[26].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[26].setText("开放");
        }

        if (bean.getDeptSo() == 0) {
            textViews[27].setTextColor(context.getResources().getColor(R.color.colorF05340));
            textViews[27].setText("不开放");
        } else {
            textViews[27].setText("开放");
        }
        if (bean.getSelfSo() == 0) {
            textViews[28].setTextColor(context.getResources().getColor(R.color.colorF05340));
            textViews[28].setText("不开放");
        } else {
            textViews[28].setText("开放");
        }
        if (bean.getPersonSo() == 0) {
            textViews[29].setTextColor(context.getResources().getColor(R.color.colorF05340));
            textViews[29].setText("不开放");
        } else {
            textViews[29].setText("开放");
        }
        if (bean.getFaultSo() == 0) {
            textViews[30].setTextColor(context.getResources().getColor(R.color.colorF05340));
            textViews[30].setText("不开放");
        } else {
            textViews[30].setText("开放");
        }

        if (bean.getSelectTaskCount() == 0) {
            textViews[31].setText("不开放");
            textViews[31].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[31].setText("开放");
        }
        if (bean.getSelectPersonCount() == 0) {
            textViews[32].setText("不开放");
            textViews[32].setTextColor(context.getResources().getColor(R.color.colorF05340));
        } else {
            textViews[32].setText("开放");
        }
    }
}
