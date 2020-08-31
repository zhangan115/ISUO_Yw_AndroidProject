package com.sito.evpro.inspection.view.equipment.archives;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

public class EquipmentArchivesActivity extends BaseActivity {

    @Inject
    EquipmentArchivesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EquipmentBean bean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        if (bean == null) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "设备详情");
        EquipmentArchivesFragment fragment = (EquipmentArchivesFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentArchivesFragment.newInstance(bean);
        }
        DaggerEquipmentArchivesComponent.builder().inspectionRepositoryComponent(InspectionApp.getInstance().getRepositoryComponent())
                .equipmentArchivesModule(new EquipmentArchivesModule(fragment)).build()
                .inject(this);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
