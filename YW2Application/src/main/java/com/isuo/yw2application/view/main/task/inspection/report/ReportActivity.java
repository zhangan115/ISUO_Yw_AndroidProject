package com.isuo.yw2application.view.main.task.inspection.report;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 提交巡检数据
 * Created by zhangan on 2017-06-26.
 */

public class ReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container, "开始巡检");
        int position = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        long taskId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        ReportFragment reportFragment = (ReportFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (reportFragment == null) {
            reportFragment = ReportFragment.newInstance(taskId,position);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), reportFragment, R.id.frame_container);
        }
    }
}
