package com.isuo.yw2application.view.main.data.fault_line;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultYearCountBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by Administrator on 2017/6/28.
 */
interface FaultLineContract {

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