package com.isuo.yw2application.view.main.work.all.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.huxq17.handygridview.HandyGridView;

public class ShowWorkItemView extends HandyGridView {
    public ShowWorkItemView(Context context) {
        super(context);
    }

    public ShowWorkItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
