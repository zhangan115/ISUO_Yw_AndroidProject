package com.sito.customer.view.home.work.inspection.data;


import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 巡检数据展示
 * Created by zhangan on 2017/9/27.
 */

public class InspectionDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "巡检数据");
        Long id = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (id == -1) {
            finish();
            return;
        }
        InspectionDataFragment fragment = (InspectionDataFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = InspectionDataFragment.newInstance(id);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
