package com.sito.customer.view.count.alarm;

import com.sito.customer.mode.bean.count.FaultCount;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-07-19.
 */

class FaultCountContract {

    interface Presenter extends BasePresenter {

        void getDeptList(String deptType);

        void getFaultCountData(String deptId, String time);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void noData();

        void showDeptList(List<DeptType> deptTypes);

        void showFaultCountData(List<FaultCount> faultCounts);
    }
}
