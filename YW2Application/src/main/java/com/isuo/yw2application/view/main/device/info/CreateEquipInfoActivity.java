package com.isuo.yw2application.view.main.device.info;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 选择对象区域，对象类型
 * Created by zhangan on 2017/10/9.
 */

public class CreateEquipInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int chooseType = getIntent().getIntExtra(ConstantStr.KEY_BUNDLE_INT, -1);
        if (chooseType == -1) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, chooseType == 0 ? "添加属地" : "添加对象类型");
        CreateEquipInfoFragment fragment = (CreateEquipInfoFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = CreateEquipInfoFragment.newInstance(chooseType);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
