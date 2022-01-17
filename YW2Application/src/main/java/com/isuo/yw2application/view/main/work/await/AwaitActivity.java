package com.isuo.yw2application.view.main.work.await;

import android.os.Bundle;
import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;

/**
 * 代办任务
 * Created by zhangan on 2018/4/17.
 */

public class AwaitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, TextUtils.isEmpty(date) ? "待办事项" : "事件任务");
        AwaitFragment fragment = (AwaitFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = AwaitFragment.newInstance("");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}
