package com.sito.customer.view.equip.data;

import android.os.Bundle;


import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
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
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
        new EquipmentDataPresenter(CustomerApp.getInstance().getEquipmentRepositoryComponent().getRepository(), fragment);
    }
}
