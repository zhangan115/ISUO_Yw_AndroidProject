package com.sito.evpro.inspection.view.equipment.data;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "巡检数据");
        long equipmentId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (equipmentId == -1) {
            finish();
            return;
        }
        EquipmentDataFragment fragment = (EquipmentDataFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentDataFragment.newInstance(equipmentId);
        }
        new EquipmentDataPresenter(InspectionApp.getInstance().getEquipmentRepositoryComponent().getRepository(), fragment);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
