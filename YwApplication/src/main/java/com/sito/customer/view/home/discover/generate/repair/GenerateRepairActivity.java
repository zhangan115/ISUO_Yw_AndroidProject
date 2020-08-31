package com.sito.customer.view.home.discover.generate.repair;


import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 生成检修任务
 * Created by zhangan on 2017/9/29.
 */

public class GenerateRepairActivity extends BaseActivity {

    @Inject
    GenerateRepairPresenter generateRepairPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "发布检修工作");
        GenerateRepairFragment fragment = (GenerateRepairFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = GenerateRepairFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
