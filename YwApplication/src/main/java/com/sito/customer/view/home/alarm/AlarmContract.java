package com.sito.customer.view.home.alarm;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.fault.AlarmCount;
import com.sito.customer.mode.bean.fault.FaultCountBean;
import com.sito.customer.mode.bean.fault.FaultDayCountBean;
import com.sito.customer.mode.bean.fault.FaultYearCountBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by Administrator on 2017/6/28.
 */
interface AlarmContract {

    interface Presenter extends BasePresenter {

        void getFaultCount();

        void getFaultDayCount(@NonNull String time);

        void getFaultYearCount(@NonNull String time);

        void getAlarmCount();
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showAlarmCount(AlarmCount alarmCount);

        void showFaultCount(FaultCountBean bean);

        void showFaultDayCount(FaultDayCountBean bean);

        void showFaultYearCount(FaultYearCountBean been);

    }

}