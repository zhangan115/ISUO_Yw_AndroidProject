package com.sito.evpro.inspection.view.greasing;

import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 注油管理合约
 * Created by Administrator on 2017/6/22.
 */
interface GreasingListContract {

    interface Presenter extends BasePresenter {

        void searchEquipment(List<InjectEquipment> allEquipment, String searchText);

        void getRoomList();

        void getRoomEquipmentList(long roomId);

        void getNeedInjectEqu(List<InjectEquipment> injectEquipments);

        void injectionEquipment(long equipmentId, Integer cycle, int position);
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void noData();

        void showRoomList(List<InjectRoomBean> roomBeanList);

        void getRoomListError();

        void showRoomEquipment(List<InjectEquipment> injectEquipments);

        void getEquipmentError();

        void injectSuccess(int position);

        void injectFail(int position);

        void showNeedInjectEqu(List<InjectEquipment> injectEquipments);
    }

}