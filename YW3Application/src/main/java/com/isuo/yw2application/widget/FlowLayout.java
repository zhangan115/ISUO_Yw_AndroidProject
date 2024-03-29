package com.isuo.yw2application.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.yw2application.R;
import com.isuo.yw2application.mode.bean.User;

import java.util.List;

/**
 * 选择流程
 * Created by zhangan on 2017/8/31.
 */

public class FlowLayout extends LinearLayout {

    private Context context;
    private LinearLayout titleLayout, userContentLayout;
    private TextView flowNameTv;
    private ImageView chooseIconIv;

    public FlowLayout(Context context) {
        super(context);
        init(context);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_choose_flow, this);
        titleLayout = (LinearLayout) findViewById(R.id.ll_title);
        userContentLayout = (LinearLayout) findViewById(R.id.ll_flow_users);
        flowNameTv = (TextView) findViewById(R.id.tv_flow_name);
        chooseIconIv = (ImageView) findViewById(R.id.iv_choose_icon);

    }

    public void setContent(String flowName, List<User> users) {
        this.setContent(flowName, users, false, false);
    }

    public void setContent(String flowName, List<User> users, boolean isChoose) {
        this.setContent(flowName, users, isChoose, true);
    }

    public void setContent(String flowName, List<User> users, boolean isChoose, boolean needChoose) {
        if (needChoose) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }
        setChooseState(isChoose);
        flowNameTv.setText(flowName);
        for (int i = 0; i < users.size(); i++) {
            userContentLayout.addView(new FlowUserLayout(context).setUser(users.get(i)));
        }
    }

    public void setChooseState(boolean isChoose) {
        if (isChoose) {
            chooseIconIv.setBackground(this.context.getResources().getDrawable(R.drawable.sos_choose_selected));
        } else {
            chooseIconIv.setBackground(this.context.getResources().getDrawable(R.drawable.sos_choose_normal));
        }
    }
}
