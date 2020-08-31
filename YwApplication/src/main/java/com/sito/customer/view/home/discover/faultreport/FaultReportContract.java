package com.sito.customer.view.home.discover.faultreport;

import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.bean.discover.FaultReport;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/4 14:44
 * E-mail：yangzongbin@si-top.com
 */
interface FaultReportContract {

    interface Presenter extends BasePresenter {
        void getChartData(String time);

        void getDeptId(String types);

        void getChartData(long deptId, String time);
    }

    interface View extends BaseView<Presenter> {

        void showDeptId(List<DeptType> deptTypes);

        void showChartData(List<FaultReport> faultReports);

        void showLoading();

        void hideLoading();

        void noData();
    }

}