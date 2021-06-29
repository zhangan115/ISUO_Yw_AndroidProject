package com.isuo.yw2application.view.main.task.overhaul.execute;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;


/**
 * 检修录入检修数据
 * Created by zhangan on 2017-06-26.
 */

public class OverhaulExecuteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "执行检修");
        long repairId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        OverhaulExecuteFragment fragment = (OverhaulExecuteFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = OverhaulExecuteFragment.newInstance(repairId);
            getFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();
        }
    }
}
