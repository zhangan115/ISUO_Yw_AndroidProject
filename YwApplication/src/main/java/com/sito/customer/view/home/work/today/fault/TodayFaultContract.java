package com.sito.customer.view.home.work.today.fault;

import com.sito.customer.mode.bean.check.FaultList;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

interface TodayFaultContract {

    interface Presenter extends BasePresenter {

        void getData(String time);
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(List<FaultList> list);

        void noData();
    }
}
