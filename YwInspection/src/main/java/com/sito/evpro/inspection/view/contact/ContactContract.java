package com.sito.evpro.inspection.view.contact;


import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 获取部门人员
 * Created by zhangan on 2017-06-26.
 */

interface ContactContract {

    interface View extends BaseView<Presenter> {

        void showData(List<DepartmentBean> list);

        void noData();

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getEmployee();
    }
}
