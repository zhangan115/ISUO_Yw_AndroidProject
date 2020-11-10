package com.isuo.yw2application.view.main.work.message.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.isuo.yw2application.R;
import com.isuo.yw2application.utils.Utils;
import com.sito.library.utils.DataUtil;

import java.text.ParseException;

/**
 * 消息item
 * Created by zhangan on 2018/4/20.
 */

public class MessageItemLayout extends LinearLayout {

    private Context context;
    private View lineView;
    private TextView tv_1, tv_2, tvTime;

    public MessageItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.message_item_layout, this);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tvTime = findViewById(R.id.tvTime);
        lineView = findViewById(R.id.lineView);
    }

    public void hideLineView() {
        lineView.setVisibility(View.GONE);
    }

    public void setData(int type, String content, long time) {
        try {
            if (time != 0) {
                if (Utils.IsToday(DataUtil.timeFormat(time, null))) {
                    tvTime.setText(DataUtil.timeFormat(time, "HH:mm"));
                } else if (Utils.IsYesterday(DataUtil.timeFormat(time, null))) {
                    tvTime.setText(String.format("昨天%s", DataUtil.timeFormat(time, "HH:mm")));
                } else {
                    tvTime.setText(DataUtil.timeFormat(time, "MM.dd HH:mm"));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(content)) {
            tv_2.setText(content);
            tv_2.setVisibility(View.VISIBLE);
        } else {
            tv_2.setVisibility(View.INVISIBLE);
        }

        String name = null;
        int backGround = -1;
        switch (type) {
            case 101://
                name = "上报";
                backGround = R.drawable.message_green;
                break;
            case 102:
                name = "关闭";
                backGround = R.drawable.message_yellow;
                break;
            case 103:
                name = "流转";
                backGround = R.drawable.message_blue;
                break;
            case 104:
                name = "转检";
                backGround = R.drawable.message_purple;
                break;
            case 201:
                name = "领取";
                backGround = R.drawable.message_green;
                break;
            case 202:
                name = "开始";
                backGround = R.drawable.message_blue;
                break;
            case 203:
                name = "结束";
                backGround = R.drawable.message_yellow;
                break;
            case 204:
                name = "发布";
                backGround = R.drawable.message_green;
                break;
            case 205:
                name = "开始";
                backGround = R.drawable.message_blue;
                break;
            case 206:
                name = "结束";
                backGround = R.drawable.message_yellow;
                break;
            case 207:
                name = "发布";
                backGround = R.drawable.message_green;
                break;
            case 208:
                name = "开始";
                backGround = R.drawable.message_blue;
                break;
            case 209:
                name = "结束";
                backGround = R.drawable.message_yellow;
                break;
            case 301:
                name = "通知";
                backGround = R.drawable.message_blue;
                break;
            case 401:
                name = "救护";
                backGround = R.drawable.message_blue;
                break;
            case 402:
                name = "火警";
                backGround = R.drawable.message_blue;
                break;
            case 501:
                name = "巡检";
                backGround = R.drawable.message_blue;
                break;
            case 502:
                name = "检修";
                backGround = R.drawable.message_green;
                break;
            case 503:
                name = "专项";
                backGround = R.drawable.message_green;
                break;
            case 701:
                name = "注油";
                backGround = R.drawable.message_purple;
                break;
            case 601:
                name = "借用";
                backGround = R.drawable.message_blue;
                break;
            case 602:
                name = "催还";
                backGround = R.drawable.message_yellow;
                break;
        }
        if (!TextUtils.isEmpty(name)) {
            tv_1.setText(name);
        } else {
            tv_1.setText("");
        }
        if (backGround == -1) {
            tv_1.setBackground(context.getResources().getDrawable(R.drawable.message_green));
        } else {
            tv_1.setBackground(context.getResources().getDrawable(backGround));
        }
        if (time == 0) {
            tv_1.setBackground(context.getResources().getDrawable(R.drawable.message_grey));
        }
    }
}
