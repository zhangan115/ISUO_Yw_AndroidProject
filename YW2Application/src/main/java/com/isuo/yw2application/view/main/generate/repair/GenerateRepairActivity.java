package com.isuo.yw2application.view.main.generate.repair;


import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
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
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "发布检修任务");
        GenerateRepairFragment fragment = (GenerateRepairFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = GenerateRepairFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
