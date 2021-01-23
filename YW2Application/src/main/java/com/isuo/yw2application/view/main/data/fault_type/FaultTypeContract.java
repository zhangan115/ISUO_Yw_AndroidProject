package com.isuo.yw2application.view.main.data.fault_type;

import com.isuo.yw2application.mode.bean.ChartData;
import com.isuo.yw2application.mode.bean.discover.FaultLevel;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/4 15:44
 * E-mail：yangzongbin@si-top.com
 */
interface FaultTypeContract {

    interface Presenter extends BasePresenter {
        void getChartData(String time);
    }

    interface View extends BaseView<Presenter> {

        void showChartData(FaultLevel faultLevels);

        void showLoading();

        void hideLoading();

        void noData();
    }

}