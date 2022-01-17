package com.isuo.yw2application.view.main.data.fault_line;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
import com.isuo.yw2application.view.contact.ContactFragment;
import com.sito.library.utils.ActivityUtils;
import com.sito.library.utils.ActivityUtilsV4;

/**
 * Created by zhangan on 2017/9/5.
 */

public class FaultLineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "事件曲线");
        FaultLineFragment fragment;
        fragment = (FaultLineFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = FaultLineFragment.newInstance();
            ActivityUtilsV4.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
