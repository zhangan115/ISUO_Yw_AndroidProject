package com.isuo.yw2application.view.main.data.count.alarm;

import com.isuo.yw2application.mode.bean.count.FaultCount;
import com.isuo.yw2application.mode.bean.discover.DeptType;
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
