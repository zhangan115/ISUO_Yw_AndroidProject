package com.isuo.yw2application.view.main.alarm.fault.history;

import com.isuo.yw2application.mode.bean.check.FaultList;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/25.
 */
interface FaultHistoryContract {

    interface Presenter extends BasePresenter {

        void getFaultList(int userId, int count);

        void getMoreFaultList(int userId, int count, long lastId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<FaultList> lists);

        void showMoreData(List<FaultList> lists);

        void noMoreData();

        void hideLoadingMore();

        void showLoading();

        void hideLoading();

        void noData();
    }

}