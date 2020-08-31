package com.sito.customer.view.equip.time.detail;

import android.os.Bundle;
import android.text.TextUtils;


import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.equip.TimeLineBean;
import com.sito.customer.view.BaseActivity;
import com.sito.library.utils.ActivityUtils;

/**
 * 检测详情,大修详情,实验详情
 * <p>
 * Created by zhangan on 2017/10/13.
 */

public class EquipmentRecordDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeLineBean lineBean = getIntent().getParcelableExtra(ConstantStr.KEY_BUNDLE_OBJECT);
        String title = null;
        if (lineBean.getType() == ConstantInt.TYPE_CHECK) {
            title = "检测详情";
        } else if (lineBean.getType() == ConstantInt.TYPE_REPAIR) {
            title = "大修详情";
        } else if (lineBean.getType() == ConstantInt.TYPE_EXPERIMENT) {
            title = "实验详情";
        }
        if (TextUtils.isEmpty(title)) {
            finish();
            return;
        }
        setLayoutAndToolbar(R.layout.activity_container_toolbar, title);
        EquipmentRecordDetailFragment fragment = (EquipmentRecordDetailFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = EquipmentRecordDetailFragment.newInstance(lineBean);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.frame_container);
        }
    }
}
