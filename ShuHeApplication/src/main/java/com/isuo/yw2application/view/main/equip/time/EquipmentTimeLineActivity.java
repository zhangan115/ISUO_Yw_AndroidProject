package com.isuo.yw2application.view.main.equip.time;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 设备图纸，维修方案
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
       if (showType == 2) {
            title = "设备图纸";
        } else {
            title = "维修方案";
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, title);
        EquipmentTimeLineFragment fragment = (EquipmentTimeLineFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentTimeLineFragment.newInstance(showType, equipmentId);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
