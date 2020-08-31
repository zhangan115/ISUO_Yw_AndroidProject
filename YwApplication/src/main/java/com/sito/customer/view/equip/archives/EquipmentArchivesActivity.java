package com.sito.customer.view.equip.archives;

import android.os.Bundle;


import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.view.BaseActivity;
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
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
        DaggerEquipmentArchivesComponent.builder().customerRepositoryComponent(CustomerApp.getInstance().getRepositoryComponent())
                .equipmentArchivesModule(new EquipmentArchivesModule(fragment)).build()
                .inject(this);
    }
}
