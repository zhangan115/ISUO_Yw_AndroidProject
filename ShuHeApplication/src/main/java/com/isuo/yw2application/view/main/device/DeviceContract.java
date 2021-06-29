package com.isuo.yw2application.view.main.device;

import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

interface DeviceContract {

    interface Presenter extends BasePresenter {

        void getEquipInfo();
    }

    interface View extends BaseView<Presenter> {

        void showData(List<EquipBean> list);

        void showLoading();

        void hideLoading();

        void noData();

    }

}