package com.sito.evpro.inspection.view.equipment.data;

import android.support.annotation.NonNull;


import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.equip.CheckData;
import com.sito.evpro.inspection.mode.bean.equip.CheckValue;
import com.sito.evpro.inspection.mode.bean.equip.InspectionData;
import com.sito.evpro.inspection.mode.equipment.EquipmentRepository;

import java.util.List;

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
