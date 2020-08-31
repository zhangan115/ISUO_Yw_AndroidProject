package com.sito.evpro.inspection.view.create.info;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import javax.inject.Inject;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/10/9.
 */

public class CreateEquipInfoActivity extends BaseActivity {

    @Inject
    CreateEquipInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int chooseType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        if (chooseType == -1) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, chooseType == 0 ? "添加属地" : "添加设备类型");
        CreateEquipInfoFragment fragment = (CreateEquipInfoFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = CreateEquipInfoFragment.newInstance(chooseType);
        }
        DaggerCreateEquipInfoComponent.builder().createRepositoryComponent(InspectionApp.getInstance().getCreateRepositoryComponent())
                .createEquipInfoModule(new CreateEquipInfoModule(fragment))
                .build().inject(this);
        ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
    }
}
