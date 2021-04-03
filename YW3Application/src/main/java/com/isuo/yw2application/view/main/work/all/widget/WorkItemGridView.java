package com.isuo.yw2application.view.main.work.all.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class WorkItemGridView extends GridView {

    public WorkItemGridView(Context context) {
        super(context);
    }

    public WorkItemGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkItemGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WorkItemGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
