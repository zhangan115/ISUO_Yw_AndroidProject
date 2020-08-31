package com.sito.evpro.inspection.view.equipment.time;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 大修记录，带电检测，实验数据
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentTimeLineActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int showType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        long equipmentId = getIntent().getLongExtra(ConstantStr.KEY_BUNDLE_LONG, -1);
        if (equipmentId == -1 || showType == -1) {
            finish();
            return;
        }
        String title;
        if (showType == 1) {
            title = "大修记录";
        } else if (showType == 2) {
            title = "带电检测";
        } else {
            title = "实验数据";
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, title);
        EquipmentTimeLineFragment fragment = (EquipmentTimeLineFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentTimeLineFragment.newInstance(showType, equipmentId);
        }
        new TimeLinePresenter(InspectionApp.getInstance().getEquipmentRepositoryComponent().getRepository(), fragment);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
