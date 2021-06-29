package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;

/**
 * Created by zhangan on 2017-08-04.
 */

public class ProgressItemLayout extends LinearLayout {

    private Context context;
    private ImageView imageView;
    private TextView tv_time;
    private TextView tv_user_progress;
    private TextView tv_content;
    private TextView tv_advice;
    private RelativeLayout contentRl;


    public ProgressItemLayout(Context context) {
        super(context);
        init(context);
    }

    public ProgressItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_progress_item, this);
        imageView = (ImageView) findViewById(R.id.icon);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_user_progress = (TextView) findViewById(R.id.tv_user_progress);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_advice = (TextView) findViewById(R.id.tv_advice);
        contentRl = findViewById(R.id.rl_content);
    }

    public void setContent(String time, String user, String content, boolean isOn) {
        if (isOn) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.work_on));
            tv_time.setTextColor(context.getResources().getColor(R.color.color535862));
            tv_user_progress.setTextColor(context.getResources().getColor(R.color.color535862));
            tv_content.setTextColor(context.getResources().getColor(R.color.color535862));
            tv_advice.setTextColor(context.getResources().getColor(R.color.color535862));
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.work_complete));
            tv_time.setTextColor(context.getResources().getColor(R.color.color90A5BF));
            tv_user_progress.setTextColor(context.getResources().getColor(R.color.color90A5BF));
            tv_content.setTextColor(context.getResources().getColor(R.color.color90A5BF));
            tv_advice.setTextColor(context.getResources().getColor(R.color.color90A5BF));
        }
        tv_time.setText(time);
        tv_user_progress.setText(user);
        tv_content.setText(content);
//        tv_content.post(new Runnable() {
//            @Override
//            public void run() {
//                Logger.d("line count is " + tv_content.getLineCount());
//                Paint.FontMetrics m = tv_content.getPaint().getFontMetrics();
//                float height = m.bottom - m.top;
////                setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
////                        , DisplayUtil.dip2px(context, 45 + height)));
//                setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                        , DisplayUtil.dip2px(context, 40 + (30 * (tv_content.getLineCount())))));
//            }
//        });
    }

    public void hideAdvice() {
        contentRl.setVisibility(View.GONE);
    }
}
