package com.sito.customer.view.home.work.create.info;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 选择设备区域，设备类型
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
        setLayoutAndToolbar(R.layout.activity_container_toolbar, chooseType == 0 ? "添加属地" : "添加设备类型");
        CreateEquipInfoFragment fragment = (CreateEquipInfoFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = CreateEquipInfoFragment.newInstance(chooseType);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
