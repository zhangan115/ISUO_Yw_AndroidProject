package com.sito.customer.view.equip.data;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.check.CheckData;
import com.sito.customer.mode.bean.check.CheckValue;
import com.sito.customer.mode.bean.check.InspectionData;
import com.sito.customer.mode.equipment.EquipmentRepository;

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
