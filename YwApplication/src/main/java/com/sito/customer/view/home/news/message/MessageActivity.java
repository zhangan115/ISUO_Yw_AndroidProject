package com.sito.customer.view.home.news.message;

import android.content.Intent;
import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.news.NewsBean;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtilsV4;

/**
 * 展示消息界面
 * Created by zhangan on 2017/11/14.
 */

public class MessageActivity extends BaseActivity {

    public static final String ACTION_OPEN_NEWS = "action.open.news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "消息");
        int position = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        NewsBean newsBean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (newsBean != null) {
            if (newsBean.isWork()) {
                position = 1;
            } else if (newsBean.isAlarm()) {
                position = 0;
            } else if (newsBean.isEnterprise()) {
                position = 2;
            }
        }
        if (position == -1) {
            finish();
            return;
        }
        MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (messageFragment == null) {
            messageFragment = MessageFragment.newInstance(position);
            ActivityUtilsV4.addFragmentToActivity(getSupportFragmentManager(), messageFragment, R.id.frame_container);
        }
    }

}
