package com.sito.evpro.inspection.view.repair.inspection.report;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
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
        if (position == -1) {
            finish();
            return;
        }
        ReportFragment reportFragment = (ReportFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (reportFragment == null) {
            reportFragment = ReportFragment.newInstance(position);
        }
        ActivityUtils.addFragmentToActivity(getFragmentManager(), reportFragment, R.id.frame_container);
        new ReportPresenter(InspectionApp.getInstance().getInspectionWorkRepositoryComponent().getRepository(), reportFragment);
    }
}
