package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.PayMenuBean;

public class PayContentView extends LinearLayout {

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
        payTitleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseListener != null && bean != null) {
                    chooseListener.chooseMenu(bean);
                }
            }
        });
        findViewById(R.id.checkDetail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public interface OnChooseListener {
        void chooseMenu(PayMenuBean bean);
    }

    private PayMenuBean bean = null;

    public void setData(PayMenuBean bean) {
        this.bean = bean;
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);
        text1.setText(bean.getMenuName());
        text2.setText(String.valueOf(bean.getPrice()));
        if (bean.getPrice() == 0) {
            text3.setText("元");
        } else {
            text3.setText("年/元");
        }

    }
}
