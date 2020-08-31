package com.sito.customer.view.home.discover.generate.increment;


import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 生成增值工作
 * Created by zhangan on 2017/9/29.
 */

public class GenerateIncrementActivity extends BaseActivity {

    @Inject
    GenerateIncrementPresenter generateIncrementPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "指派专项工作");
        GenerateIncrementFragment fragment = (GenerateIncrementFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = GenerateIncrementFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
