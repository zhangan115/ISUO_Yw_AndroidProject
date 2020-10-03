package com.isuo.yw2application.view.main.equip.time.detail;

import android.os.Bundle;
import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.equip.TimeLineBean;
import com.isuo.yw2application.view.base.BaseActivity;
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
