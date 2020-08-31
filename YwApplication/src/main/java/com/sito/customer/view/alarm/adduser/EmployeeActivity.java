package com.sito.customer.view.alarm.adduser;

import android.os.Bundle;

import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.bean.employee.EmployeeBean;
import com.sito.customer.view.BaseActivity;
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
