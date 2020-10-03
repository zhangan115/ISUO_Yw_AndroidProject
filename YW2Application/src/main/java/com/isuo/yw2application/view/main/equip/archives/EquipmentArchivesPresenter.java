package com.isuo.yw2application.view.main.equip.archives;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

public class EquipmentArchivesPresenter implements EquipmentArchivesContract.Presenter {

    private final CustomerRepository mRepository;
    private final EquipmentArchivesContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    EquipmentArchivesPresenter(CustomerRepository mRepository, EquipmentArchivesContract.View mView) {
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
    public void getEquipmentDetail(long equipmentId) {
        subscription.add(mRepository.getEquipmentDetail(equipmentId, new IObjectCallBack<EquipmentBean>() {
            @Override
            public void onSuccess(@NonNull EquipmentBean bean) {
                mView.showEquipmentDetail(bean);
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
    public void getEquipmentCare(long equipmentId) {
        subscription.add(mRepository.getCareEquipmentData(equipmentId, new IObjectCallBack<FocusBean>() {
            @Override
            public void onSuccess(@NonNull FocusBean s) {
                mView.showCareData(s);
            }

            @Override
            public void onError(String message) {
                mView.showCareDataFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
