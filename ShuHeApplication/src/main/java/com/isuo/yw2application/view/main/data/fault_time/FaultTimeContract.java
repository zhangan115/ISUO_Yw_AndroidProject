package com.isuo.yw2application.view.main.data.fault_time;

import com.isuo.yw2application.mode.bean.ChartData;
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

    interface View extends BaseView<Presenter> {
        void showData(List<ChartData> chartDatas);

        void showLoading();

        void hideLoading();
    }

}