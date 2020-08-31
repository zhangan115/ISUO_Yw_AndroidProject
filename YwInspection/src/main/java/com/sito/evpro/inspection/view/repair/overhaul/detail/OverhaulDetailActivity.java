package com.sito.evpro.inspection.view.repair.overhaul.detail;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;


/**
 * 检修详情
 * Created by zhangan on 2017-07-03.
 */

public class OverhaulDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "检修详情");
        long repairId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (repairId == -1) {
            finish();
            return;
        }
        OverhaulDetailFragment fragment = (OverhaulDetailFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = OverhaulDetailFragment.newInstance(repairId);
        }
        new OverhaulDetailPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), fragment);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

}
