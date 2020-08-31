package com.sito.customer.view.equip.alarm;

import android.support.annotation.NonNull;


import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.equipment.EquipmentRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by pingan on 2017/7/3.
 */

 class EquipmentAlarmPresenter implements EquipmentAlarmContact.Presenter {

    private EquipmentRepository mRepository;
    private EquipmentAlarmContact.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

     EquipmentAlarmPresenter(EquipmentRepository repository, EquipmentAlarmContact.View view) {
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
    public void getFaultByEId(long equipId) {
        mView.showLoading();
        mSubscription.add(mRepository.getFaultByEId(equipId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFault(list);
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
    public void getMoreFaultById(long equipId, int lastId) {
        mSubscription.add(mRepository.getMoreFaultByEId(equipId, lastId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFault(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }
}
