package com.sito.customer.view.equip.time;

import android.support.annotation.NonNull;


import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.equip.TimeLineBean;
import com.sito.customer.mode.equipment.EquipmentRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017/10/13.
 */

class TimeLinePresenter implements EquipmentTimeLineContract.Presenter {

    private final EquipmentRepository mRepository;
    private final EquipmentTimeLineContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    TimeLinePresenter(EquipmentRepository mRepository, EquipmentTimeLineContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void getEquipRepairData(long equipmentId) {
        mView.showLoading();
        mSubscription.add(mRepository.getEquipRepairRecordData(equipmentId, new IListCallBack<TimeLineBean>() {
            @Override
            public void onSuccess(@NonNull List<TimeLineBean> list) {
                mView.showData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getEquipCheckData(long equipmentId) {
        mView.showLoading();
        mSubscription.add(mRepository.getEquipCheckData(equipmentId, new IListCallBack<TimeLineBean>() {
            @Override
            public void onSuccess(@NonNull List<TimeLineBean> list) {
                mView.showData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getEquipExperimentData(long equipmentId) {
        mView.showLoading();
        mSubscription.add(mRepository.getEquipExperimentData(equipmentId, new IListCallBack<TimeLineBean>() {
            @Override
            public void onSuccess(@NonNull List<TimeLineBean> list) {
                mView.showData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }
}
