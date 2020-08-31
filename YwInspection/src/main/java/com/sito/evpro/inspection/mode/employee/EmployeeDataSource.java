package com.sito.evpro.inspection.mode.employee;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;

import rx.Subscription;

/**
 * 获取部门人员选择
 * Created by zhangan on 2017-07-07.
 */

public interface EmployeeDataSource {

    @NonNull
    Subscription getEmployeeList(@NonNull IListCallBack<DepartmentBean> callBack);

    void cleanCache();
}
