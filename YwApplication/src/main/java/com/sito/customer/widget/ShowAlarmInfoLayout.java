package com.sito.customer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.customer.R;

import java.text.MessageFormat;

/**
 * Created by pingan on 2017/7/2.
 */

public class ShowAlarmInfoLayout extends LinearLayout {

    private Context mContext;
    private ImageView circleView;
    private TextView timeTv, userTv, dealTv;

    public ShowAlarmInfoLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ShowAlarmInfoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_alarm_info, this, true);
        circleView = (ImageView) findViewById(R.id.iv_circle);
        timeTv = (TextView) findViewById(R.id.tv_time);
        userTv = (TextView) findViewById(R.id.tv_user);
        dealTv = (TextView) findViewById(R.id.tv_deal);
    }

    public ShowAlarmInfoLayout setContent(String time, String user, String deal, Drawable drawable) {
        timeTv.setText(time);
        userTv.setText(user);
        if (TextUtils.isEmpty(deal)) {
            deal = "";
        }
        dealTv.setText(MessageFormat.format("处理意见:{0}", deal));
        circleView.setImageDrawable(drawable);
        return this;
    }
}
