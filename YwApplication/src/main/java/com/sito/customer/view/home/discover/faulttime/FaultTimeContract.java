package com.sito.customer.view.home.discover.faulttime;

import com.sito.customer.mode.bean.ChartData;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/4 15:33
 * E-mail：yangzongbin@si-top.com
 */
interface FaultTimeContract {

    interface Presenter extends BasePresenter {
        void getChartData(String time);
    }

    interface View extends BaseView<FaultTimeContract.Presenter> {
        void showData(List<ChartData> chartDatas);

        void showLoading();

        void hideLoading();
    }

}