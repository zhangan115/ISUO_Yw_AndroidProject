package com.isuo.yw2application.view.main.equip.record;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.equipment.EquipmentRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by pingan on 2017/7/3.
 */

class EquipmentRecordPresenter implements EquipmentRecordContact.Presenter {

    private EquipmentRepository mRepository;
    private EquipmentRecordContact.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    EquipmentRecordPresenter(EquipmentRepository repository, EquipmentRecordContact.View view) {
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
    public void getOverByEId(long equipId) {
        mView.showLoading();
        mSubscription.add(mRepository.getOverByEId(equipId, new IListCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull List<OverhaulBean> list) {
                mView.showData(list);
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
    public void getMoreOverByEId(long equipId, int lastId) {
        mSubscription.add(mRepository.getMoreOverByEId(equipId, lastId, new IListCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull List<OverhaulBean> list) {
                mView.showMoreData(list);
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
