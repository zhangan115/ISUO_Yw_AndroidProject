package com.sito.customer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;

/**
 * Created by zhangan on 2017-07-19.
 */

public class FaultTitleView extends LinearLayout {

    private ImageView icon;
    private TextView titleTv;

    public FaultTitleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_fault_title, this);
        icon = (ImageView) findViewById(R.id.icon);
        titleTv = (TextView) findViewById(R.id.tv_title);
    }

    public FaultTitleView setContent(Drawable drawable, String title, boolean isActive) {
        icon.setImageDrawable(drawable);
        titleTv.setText(title);
        return this;
    }
}
