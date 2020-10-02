package com.isuo.yw2application.view.main.work.message;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtilsV4;

public class NewsListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar);
        int type = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT,0);
        if (getTitleTv()==null)return;
        switch (type){
            case 0:
                getTitleTv().setText("工作动态");
                break;
            case 1:
                getTitleTv().setText("故障消息");
                break;
            case 2:
                getTitleTv().setText("企业通知");
                break;
            case 3:
                getTitleTv().setText("与我相关");
                break;
        }
        NewsListFragment fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = NewsListFragment.newInstance(type);
            ActivityUtilsV4.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
