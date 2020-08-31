package com.sito.evpro.inspection.view.equipment.alarm;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentAlarmActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "故障记录");
        long equipmentId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (equipmentId == -1) {
            finish();
            return;
        }
        EquipmentAlarmFragment fragment = (EquipmentAlarmFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentAlarmFragment.newInstance(equipmentId);
        }
        new EquipmentAlarmPresenter(InspectionApp.getInstance().getEquipmentRepositoryComponent().getRepository(), fragment);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
