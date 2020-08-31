package com.sito.customer.view.home.work.overhaul.execute;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;


/**
 * 检修录入检修数据
 * Created by zhangan on 2017-06-26.
 */

public class OverhaulExecuteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "检修");
        long repairId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        OverhaulExecuteFragment fragment = (OverhaulExecuteFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = OverhaulExecuteFragment.newInstance(repairId);
            getFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();
        }
    }
}
