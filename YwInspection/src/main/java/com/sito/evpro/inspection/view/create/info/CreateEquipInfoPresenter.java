package com.sito.evpro.inspection.view.create.info;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.create.ChooseRoomOrType;
import com.sito.evpro.inspection.mode.create.CreateRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/10/9.
 */

class CreateEquipInfoPresenter implements CreateEquipInfoContract.Presenter {

    private final CreateRepository mRepository;
    private final CreateEquipInfoContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    CreateEquipInfoPresenter(CreateRepository mRepository, CreateEquipInfoContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        subscription = new CompositeSubscription();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
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
    public void addEquipmentType(@NonNull String equipmentType) {
        subscription.add(mRepository.addEquipmentType(equipmentType, new IObjectCallBack<String>() {
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
