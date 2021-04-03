package com.isuo.yw2application.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;

/**
 * Created by zhangan on 2017-08-02.
 */

public class ProgressTitleLayout extends LinearLayout {

    private ImageView iconIv;
    private TextView titleTv;
    private Context context;
    private LinearLayout ll_item;

    public ProgressTitleLayout(Context context) {
        super(context);
        init(context);
    }

    public ProgressTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_progress, this);
        iconIv = (ImageView) findViewById(R.id.iv_icon);
        titleTv = (TextView) findViewById(R.id.tv_title_name);
        ll_item = (LinearLayout) findViewById(R.id.ll_item);
    }

    public void setContent(Drawable drawableRes, String title, boolean isOn) {
        titleTv.setText(title);
        iconIv.setImageDrawable(drawableRes);
        if (isOn) {
            titleTv.setTextColor(context.getResources().getColor(R.color.color69A7FB));
        } else {
            titleTv.setTextColor(context.getResources().getColor(R.color.color90a5bf));
        }
    }

    public void addItem(View view) {
        ll_item.addView(view);
    }
}
