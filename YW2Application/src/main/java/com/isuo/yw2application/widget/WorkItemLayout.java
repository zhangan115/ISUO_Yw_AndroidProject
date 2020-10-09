package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.work.WorkItem;

/**
 * 选择流程
 * Created by zhangan on 2017/8/31.
 */

public class WorkItemLayout extends LinearLayout {

    private Context context;
    private TextView textView;
    private ImageView imageView;

    public WorkItemLayout(Context context) {
        super(context);
        init(context);
    }

    public WorkItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_work_item, this);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
    }

    public void setContent(WorkItem item) {
        textView.setText(item.getName());
        setTag(item.getId());
        imageView.setImageDrawable(context.getResources().getDrawable(item.getIcon()));
    }
}
