package com.sito.customer.view.alarm.adduser;

import com.sito.customer.mode.bean.employee.DepartmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 获取部门人员
 * Created by zhangan on 2017-06-26.
 */

interface EmployeeContract {

    interface View extends BaseView<Presenter> {

        void showData(List<DepartmentBean> list);

        void noData();

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getEmployee(boolean isRepair);
    }
}
