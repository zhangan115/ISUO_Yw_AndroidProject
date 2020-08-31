package com.sito.customer.view.home.work.inject;

import com.sito.customer.mode.inject.bean.InjectEquipment;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * 注油契约
 * Created by zhangan on 2017/9/21.
 */

interface InjectContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void noData();

        void showRoomEquipment(List<InjectEquipment> injectEquipments);

        void getEquipmentError();

        void showSearchInjectEqu(List<InjectEquipment> injectEquipments);
    }

    interface Presenter extends BasePresenter {

        void searchEquipment(List<InjectEquipment> allEquipment, String searchText);

        void getRoomEquipmentList(Map<String, String> map);

    }

}
