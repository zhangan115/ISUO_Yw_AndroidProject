package com.isuo.yw2application.view.main.data.staff_time;

import com.isuo.yw2application.mode.bean.ChartData;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.mode.bean.discover.FaultReport;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/4 16:02
 * E-mail：yangzongbin@si-top.com
 */
interface StaffTimeContract {

    interface Presenter extends BasePresenter {
        void getChartData(String time);

        void getDeptId(String types);

        void getChartData(long deptId, String time);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<ChartData> chartDatas);

        void showDeptId(List<DeptType> deptTypes);

        void showChartData(List<FaultReport> faultReports);

        void showLoading();

        void hideLoading();

        void noData();
    }

}