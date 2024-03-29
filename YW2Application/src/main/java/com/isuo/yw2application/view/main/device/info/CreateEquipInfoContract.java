package com.isuo.yw2application.view.main.device.info;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 选择对象区域，对象类型
 * Created by zhangan on 2017/9/30.
 */

interface CreateEquipInfoContract {

    interface Presenter extends BasePresenter {

        void getRoomList();

        void getEquipmentList();

        void addRoom(int type, @NonNull String roomName);

        void addEquipmentType(Long parentId, Integer level, @NonNull String equipmentType);

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
