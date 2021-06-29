package com.isuo.yw2application.view.main.alarm.list;

import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-30.
 */

interface AlarmListContact {

    interface Presenter extends BasePresenter {

        void getEquipmentType();

        void getFaultList(@Nullable String equipmentType, @Nullable String alarmType, @Nullable String alarmState
                , @Nullable String startTimeStr, @Nullable String endTimeStr);

        void getFaultList(@Nullable String equipmentType, @Nullable String alarmType, @Nullable String alarmState
                , @Nullable String startTimeStr, @Nullable String endTimeStr, @Nullable String lastId);

    }

    interface View extends BaseView<Presenter> {

        void showEquipmentType(List<EquipType> equipmentTypeBeen);

        void showFaultList(List<FaultList> list);

        void showFaultListMore(List<FaultList> list);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }
}
