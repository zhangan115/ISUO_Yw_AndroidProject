package com.sito.customer.view.home.discover.faulttype;

import com.sito.customer.mode.bean.ChartData;
import com.sito.customer.mode.bean.discover.FaultLevel;
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
        void showData(List<ChartData> chartDatas);

        void showChartData(List<FaultLevel> faultLevels);

        void showLoading();

        void hideLoading();

        void noData();
    }

}