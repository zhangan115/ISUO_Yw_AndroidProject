package com.sito.evpro.inspection.view.equipment.alarm;

import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by pingan on 2017/7/3.
 */

interface EquipmentAlarmContact {
    interface Presenter extends BasePresenter {
        void getFaultByEId(long equipId);

        void getMoreFaultById(long equipId, int lastId);
    }

    interface View extends BaseView<Presenter> {
        void showFault(List<FaultList> lists);

        void showMoreFault(List<FaultList> lists);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }
}
