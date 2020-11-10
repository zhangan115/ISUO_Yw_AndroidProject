package com.isuo.yw2application.view.main.alarm.fault.today;

import com.isuo.yw2application.mode.bean.check.FaultList;
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
