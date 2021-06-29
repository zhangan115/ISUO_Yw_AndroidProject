package com.isuo.yw2application.view.main.adduser;

import android.os.Bundle;

import com.isuo.yw2application.R;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.view.base.BaseActivity;
import com.sito.library.utils.ActivityUtils;

import java.util.ArrayList;

/**
 * 人员列表
 * Created by zhangan on 2017-06-26.
 */

public class EmployeeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_container_toolbar, "添加人员");
        ArrayList<EmployeeBean> employeeBeans = getIntent().getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
        EmployeeFragment employeeFragment = (EmployeeFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        boolean isRepair = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN, false);
        boolean isChooseOne = getIntent().getBooleanExtra(ConstantStr.KEY_BUNDLE_BOOLEAN_1, false);
        String[] userIds = getIntent().getStringArrayExtra(ConstantStr.KEY_BUNDLE_LIST_1);
        if (employeeFragment == null) {
            employeeFragment = EmployeeFragment.newInstance(employeeBeans, isRepair, isChooseOne,userIds);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), employeeFragment, R.id.frame_container);
        }
    }
}
