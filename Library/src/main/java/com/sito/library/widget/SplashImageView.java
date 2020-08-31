package com.sito.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 启动欢迎界面，防止穿透点击
 * Created by zhangan on 2017-03-06.
 */

public class SplashImageView extends android.support.v7.widget.AppCompatImageView {

    public SplashImageView(Context context) {
        super(context);
    }

    public SplashImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplashImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
}
