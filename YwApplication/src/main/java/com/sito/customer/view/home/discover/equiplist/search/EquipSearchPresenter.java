package com.sito.customer.view.home.discover.equiplist.search;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.equip.EquipRoom;
import com.sito.customer.mode.bean.equip.EquipType;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/17 14:54
 * E-mail：yangzongbin@si-top.com
 */
final class EquipSearchPresenter implements EquipSearchContract.Presenter {
    private CustomerRepository mRepository;
    private EquipSearchContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    EquipSearchPresenter(CustomerRepository repository, EquipSearchContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getEquipType() {
        mView.showLoading();
        mSubscruptions.add(mRepository.getEquipType(new IListCallBack<EquipType>() {
            @Override
            public void onSuccess(@NonNull List<EquipType> list) {
                mView.showEquipType(list);
            }

            @Override
            public void onError(String message) {
                mView.noDdata();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getEquipPlace() {
        mView.showLoading();
        mSubscruptions.add(mRepository.getEquipRoom(new IListCallBack<EquipRoom>() {
            @Override
            public void onSuccess(@NonNull List<EquipRoom> list) {
                mView.showEquipRoom(list);
            }

            @Override
            public void onError(String message) {
                mView.noDdata();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}