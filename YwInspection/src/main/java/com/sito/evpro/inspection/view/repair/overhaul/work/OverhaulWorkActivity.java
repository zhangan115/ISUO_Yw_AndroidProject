package com.sito.evpro.inspection.view.repair.overhaul.work;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;

/**
 * 检修录入检修数据
 * Created by zhangan on 2017-06-26.
 */

public class OverhaulWorkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "执行检修");
        String repairId = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        OverhaulWorkFragment fragment = (OverhaulWorkFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = OverhaulWorkFragment.newInstance(repairId);
        }
        new OverhaulWorkPresenter(InspectionApp.getInstance().getRepositoryComponent().getRepository(), fragment);
        getFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();
    }
}
