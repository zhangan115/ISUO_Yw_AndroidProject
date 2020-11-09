package com.isuo.yw2application.view.main.alarm;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
interface AlarmContract {

    interface Presenter extends BasePresenter {

        void getWorkCount();

        void getAlarmCount();

        void getAlarmList();

        void getFaultDayCount(@NonNull String time);

    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showFaultList(List<FaultList> list);

        void showAlarmCount(AlarmCount bean);

        void showFaultDayCount(FaultDayCountBean bean);

        void showWorkState(WorkState workState);

    }

}