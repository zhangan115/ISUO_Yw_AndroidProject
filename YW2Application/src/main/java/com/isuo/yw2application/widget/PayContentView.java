package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;

public class PayContentView extends LinearLayout {

    public PayContentView(Context context) {
        super(context);
        init(context);
    }

    public PayContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.layout_pay_content, this);
        findViewById(R.id.payTitle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.checkDetail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
