package com.sito.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sito.library.R;
import com.sito.library.utils.GlideUtils;

/**
 * 显示用户信息
 * Created by zhangan on 2017/11/15.
 */

@SuppressLint("ViewConstructor")
public class ShowUserLayout extends LinearLayout {

    public ShowUserLayout(Context context, String userName, String userPhoto, @ColorInt int color) {
        super(context);
        inflate(context, R.layout.layout_user_show, this);
        ImageView imageView = findViewById(R.id.iv_user);
        TextView textView = findViewById(R.id.tv_user);
        GlideUtils.ShowCircleImage(context, userPhoto, imageView, R.drawable.user_head_libary);
        textView.setTextColor(color);
        textView.setText(userName);
    }

}
