package com.isuo.yw2application.view.main.device.info;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.isuo.yw2application.mode.create.CreateDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/10/9.
 */

class CreateEquipInfoPresenter implements CreateEquipInfoContract.Presenter {

    private final CreateDataSource mRepository;
    private final CreateEquipInfoContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    CreateEquipInfoPresenter(CreateDataSource mRepository, CreateEquipInfoContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getRoomList() {
        subscription.add(mRepository.getRoomList(new IListCallBack<ChooseRoomOrType>() {
            @Override
            public void onSuccess(@NonNull List<ChooseRoomOrType> list) {
                mView.showRoomOrTypeList(list);
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getEquipmentList() {
        mView.showLoading();
        subscription.add(mRepository.getEquipmentTypeList(new IListCallBack<ChooseRoomOrType>() {
            @Override
            public void onSuccess(@NonNull List<ChooseRoomOrType> list) {
                mView.showRoomOrTypeList(list);
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void addRoom(int roomType, @NonNull String roomName) {
        subscription.add(mRepository.addRoom(roomType, roomName, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String str) {
                mView.addRoomOrTypeSuccess();
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void addEquipmentType(Long parentId, Integer level, @NonNull String equipmentType) {
        subscription.add(mRepository.addEquipmentType(parentId,level,equipmentType, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String chooseRoomOrType) {
                mView.addRoomOrTypeSuccess();
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void deleteRoom(long roomId) {
        subscription.add(mRepository.deleteRoom(roomId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.deleteRoomSuccess();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void deleteEquipmentType(long equipmentTypeId) {
        subscription.add(mRepository.deleteEquipmentType(equipmentTypeId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.deleteEquipmentTypeSuccess();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
