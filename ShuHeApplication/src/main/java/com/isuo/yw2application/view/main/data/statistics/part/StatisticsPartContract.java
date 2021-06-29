package com.isuo.yw2application.view.main.data.statistics.part;

import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
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
