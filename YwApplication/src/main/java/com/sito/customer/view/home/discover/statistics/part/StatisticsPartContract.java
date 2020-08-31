package com.sito.customer.view.home.discover.statistics.part;

import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 统计
 * Created by zhangan on 2018/4/18.
 */

interface StatisticsPartContract {

    interface Presenter extends BasePresenter {

        void getCStatisticsPartData(String startTime, String endTime);

    }

    interface View extends BaseView<Presenter> {


        void showData(PartPersonStatistics statistics);


        void showLoading();

        void hideLoading();

    }
}
