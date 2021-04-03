package com.isuo.yw2application.view.main.device;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/15 12:35
 * E-mail：yangzongbin@si-top.com
 */
final class DevicePresenter implements DeviceContract.Presenter {
    private CustomerRepository mRepository;
    private DeviceContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    DevicePresenter(CustomerRepository repository, DeviceContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void getEquipInfo() {
        mView.showLoading();
        subscription.add(mRepository.getEquipInfo(false, new IListCallBack<EquipBean>() {
            @Override
            public void onSuccess(@NonNull List<EquipBean> list) {
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
    public void unSubscribe() {
        subscription.clear();
    }
}