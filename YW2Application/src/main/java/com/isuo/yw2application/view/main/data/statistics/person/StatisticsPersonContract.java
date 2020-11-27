package com.isuo.yw2application.view.main.data.statistics.person;

import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 统计个人
 * Created by zhangan on 2018/4/18.
 */

interface StatisticsPersonContract {

    interface Presenter extends BasePresenter {

        void getCStatisticsPersonData(String startTime, String endTime,int userId);
    }

    interface View extends BaseView<Presenter> {

        void showData(PartPersonStatistics statistics);

        void showLoading();

        void hideLoading();
    }
}
