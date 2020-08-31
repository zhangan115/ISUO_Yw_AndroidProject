package com.sito.customer.view.home.work.create.equipment;

import android.support.annotation.NonNull;


import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.create.CreateDataSource;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */

class CreateEquipPresenter implements CreateEquipContract.Presenter {

    private final CreateDataSource mRepository;
    private final CreateEquipContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    CreateEquipPresenter(CreateDataSource mRepository, CreateEquipContract.View mView) {
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
    public void uploadImage(@NonNull String businessType, @NonNull String path) {
        subscription.add(mRepository.uploadPhoto(businessType, path, new IObjectCallBack<List<String>>() {
            @Override
            public void onSuccess(@NonNull List<String> strings) {
                mView.showImage(strings.get(0));
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
    public void uploadEquipment(JSONObject jsonObject) {
        mView.showLoading();
        subscription.add(mRepository.addEquipment(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadEquipmentSuccess();
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
    public void editEquipment(JSONObject jsonObject) {
        mView.showLoading();
        subscription.add(mRepository.editEquipment(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadEquipmentSuccess();
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
}
