package com.sito.customer.view.home.work.await;

import android.os.Bundle;
import android.text.TextUtils;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;

/**
 * 代办任务
 * Created by zhangan on 2018/4/17.
 */

public class AwaitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, TextUtils.isEmpty(date) ? "待办事项" : "今日代办");
        AwaitFragment fragment = (AwaitFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = AwaitFragment.newInstance(date);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}
