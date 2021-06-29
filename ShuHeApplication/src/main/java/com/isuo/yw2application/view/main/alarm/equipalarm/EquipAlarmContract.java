package com.isuo.yw2application.view.main.alarm.equipalarm;

import com.isuo.yw2application.mode.bean.check.FaultList;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 作者：Yangzb on 2017/6/30 10:59
 * 邮箱：yangzongbin@si-top.com
 */
interface EquipAlarmContract {

    interface Presenter extends BasePresenter {

        void getTodayFault(String startTime, String endTime);

        void getTodayFault(String startTime, String endTime, int alarmType);

        void getFaultList();//遗留故障统计

        void getFaultList(int alarmType);//遗留故障统计

        void getMoreFaultList(long lastId);

        void getMoreFaultList(long lastId, int alarmType);

        void getMoreTodayFault(long lastId, String startTime, String endTime);

        void getMoreTodayFault(long lastId, String startTime, String endTime, int alarmType);
    }

    interface View extends BaseView<Presenter> {
        void showFaultList(List<FaultList> faultLists);

        void showMoreFaultList(List<FaultList> faultLists);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }

}