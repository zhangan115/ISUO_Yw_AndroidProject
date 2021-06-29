package com.isuo.yw2application.view.contact;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 联系人界面
 * Created by zhangan on 2017/9/5.
 */

public class ContactActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "联系人");
        ContactFragment fragment;
        fragment = (ContactFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
             fragment= ContactFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
