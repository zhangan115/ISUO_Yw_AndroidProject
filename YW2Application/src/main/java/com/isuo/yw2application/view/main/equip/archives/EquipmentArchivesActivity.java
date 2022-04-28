package com.isuo.yw2application.view.main.equip.archives;

import android.os.Bundle;
import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 对象档案
 * Created by zhangan on 2017/10/12.
 */

public class EquipmentArchivesActivity extends BaseActivity {

    @Inject
    EquipmentArchivesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "对象详情");
        EquipmentBean bean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        String equipmentIdResult = getIntent().getStringExtra(ConstantStr.KEY_BUNDLE_STR);
        if (bean == null && TextUtils.isEmpty(equipmentIdResult)) {
            finish();
            return;
        }
        EquipmentArchivesFragment fragment = (EquipmentArchivesFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (!TextUtils.isEmpty(equipmentIdResult)){
            if (fragment == null) {
                fragment = EquipmentArchivesFragment.newInstance(equipmentIdResult);
                ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
            }
        }
        if (bean!=null){
            if (fragment == null) {
                fragment = EquipmentArchivesFragment.newInstance(bean);
                ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
            }
        }
        DaggerEquipmentArchivesComponent.builder().customerRepositoryComponent(Yw2Application.getInstance().getRepositoryComponent())
                .equipmentArchivesModule(new EquipmentArchivesModule(fragment)).build()
                .inject(this);
    }
}
