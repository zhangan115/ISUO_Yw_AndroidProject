package com.sito.evpro.inspection.view.repair.inspection.data;


import android.os.Bundle;


import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 巡检数据展示
 * Created by zhangan on 2017/9/27.
 */

public class InspectionDataActivity extends BaseActivity {

    @Inject
    InspectionDataPresenter inspectionPresenter;

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
        }
        DaggerInspectionDataComponent.builder().inspectionWorkRepositoryComponent(InspectionApp.getInstance().getInspectionWorkRepositoryComponent())
                .inspectionDataModule(new InspectionDataModule(fragment)).build().inject(this);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
