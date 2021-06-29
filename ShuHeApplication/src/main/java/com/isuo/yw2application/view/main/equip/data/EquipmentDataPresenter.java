package com.isuo.yw2application.view.main.equip.data;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.CheckValue;
import com.isuo.yw2application.mode.bean.check.InspectionData;
import com.isuo.yw2application.mode.equipment.EquipmentRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by pingan on 2017/7/3.
 */

class EquipmentDataPresenter implements EquipmentDataContact.Presenter {

    private EquipmentRepository mRepository;
    private EquipmentDataContact.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    EquipmentDataPresenter(EquipmentRepository repository, EquipmentDataContact.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void getCheckData(long equipId) {
        mView.showLoading();
        mSubscription.add(mRepository.getCheckData(equipId, new IObjectCallBack<InspectionData>() {
            @Override
            public void onSuccess(@NonNull InspectionData list) {
                mView.showCheckData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }


    @Override
    public void getCheckValue(long equipId, int inspecId) {
        mView.showLoading();
        mSubscription.add(mRepository.getCheckValue(equipId, inspecId, new IObjectCallBack<CheckValue>() {
            @Override
            public void onSuccess(@NonNull CheckValue checkValue) {
                mView.showCheckValue(checkValue);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }
}
