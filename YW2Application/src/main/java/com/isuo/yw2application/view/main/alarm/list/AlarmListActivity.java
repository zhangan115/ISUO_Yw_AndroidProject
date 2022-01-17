package com.isuo.yw2application.view.main.alarm.list;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 故障列表
 * Created by zhangan on 2017-06-30.
 */

public class AlarmListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "事件列表");
        AlarmListFragment fragment = (AlarmListFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = AlarmListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }

}
