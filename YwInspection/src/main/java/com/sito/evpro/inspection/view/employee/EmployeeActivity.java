package com.sito.evpro.inspection.view.employee;

import android.os.Bundle;

import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.view.BaseActivity;
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
        ArrayList<EmployeeBean> employeeBeen = getIntent().getParcelableArrayListExtra(ConstantStr.KEY_BUNDLE_LIST);
        String[] userIds = getIntent().getStringArrayExtra(ConstantStr.KEY_BUNDLE_LIST_1);
        EmployeeFragment employeeFragment = (EmployeeFragment) getFragmentManager().findFragmentById(R.id.frame_container);
        if (employeeFragment == null) {
            if (employeeBeen != null) {
                employeeFragment = EmployeeFragment.newInstance(employeeBeen);
            } else {
                employeeFragment = EmployeeFragment.newInstance(userIds);
            }
        }
        ActivityUtils.addFragmentToActivity(getFragmentManager(), employeeFragment, R.id.frame_container);
        new EmployeePresenter(InspectionApp.getInstance().getEmployeeRepositoryComponent().getRepository(), employeeFragment);
    }

}
