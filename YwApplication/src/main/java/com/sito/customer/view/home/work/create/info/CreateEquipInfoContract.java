package com.sito.customer.view.home.work.create.info;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.create.ChooseRoomOrType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/9/30.
 */

interface CreateEquipInfoContract {

    interface Presenter extends BasePresenter {

        void getRoomList();

        void getEquipmentList();

        void addRoom(int type, @NonNull String roomName);

        void addEquipmentType(@NonNull String equipmentType);

        void deleteRoom(long roomId);

        void deleteEquipmentType(long equipmentTypeId);
    }

    interface View extends BaseView<Presenter> {

        void showRoomOrTypeList(List<ChooseRoomOrType> chooseRoomOrTypes);

        void addRoomOrTypeSuccess();

        void showMessage(String message);

        void showLoading();

        void hideLoading();

        void deleteRoomSuccess();

        void deleteEquipmentTypeSuccess();
    }
}
