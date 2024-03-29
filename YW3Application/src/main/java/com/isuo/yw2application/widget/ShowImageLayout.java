package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.photo.ViewPagePhotoActivity;
import com.sito.library.utils.DisplayUtil;
import com.sito.library.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangan on 2017-08-01.
 */

public class ShowImageLayout extends LinearLayout {

    private LinearLayout ll_image;
    private Context mContext;
    private String[] urls;

    public ShowImageLayout(Context context) {
        super(context);
        init(context);
    }

    public ShowImageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.layout_show_image, this);
        ll_image = (LinearLayout) findViewById(R.id.ll_image);
    }

    public void showImage(String[] url) {
        List<String> urlList = new ArrayList<>();
        for (String anUrl : url) {
            if (!TextUtils.isEmpty(anUrl)) {
                urlList.add(anUrl);
            }
        }
        if (urlList.size() > 0) {
            urls = new String[urlList.size()];
            for (int i = 0; i < urlList.size(); i++) {
                urls[i] = urlList.get(i);
            }
        } else {
            urls = null;
        }
        ll_image.removeAllViews();
        if (urls == null || urls.length == 0) {
            setVisibility(View.INVISIBLE);
        } else {
            setVisibility(View.VISIBLE);
            for (int i = 0; i < urls.length; i++) {
                ll_image.addView(createImage(i, url[i]));
            }
        }
    }

    private ImageView createImage(final int position, String path) {
        final ImageView imageView = new ImageView(mContext);
        int width = DisplayUtil.dip2px(mContext, 64);
        int height = DisplayUtil.dip2px(mContext, 64);
        LayoutParams params = new LayoutParams(width, height);
        imageView.setLayoutParams(params);
        if (position != 0) {
            params.setMargins(DisplayUtil.dip2px(mContext, 20), 0, 0, 0);
        }
        imageView.setTag(R.id.tag_position, position);
        imageView.setOnClickListener(onClickListener);
        GlideUtils.ShowImage(mContext, path, imageView, R.drawable.img_default);
        return imageView;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_position);
            ViewPagePhotoActivity.startActivity(mContext, urls, position);
        }
    };
}
