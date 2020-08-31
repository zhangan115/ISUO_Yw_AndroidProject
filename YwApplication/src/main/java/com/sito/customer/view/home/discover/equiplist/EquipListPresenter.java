package com.sito.customer.view.home.discover.equiplist;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.equip.EquipBean;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/15 12:35
 * E-mail：yangzongbin@si-top.com
 */
final class EquipListPresenter implements EquipListContract.Presenter {
    private CustomerRepository mRepository;
    private EquipListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    EquipListPresenter(CustomerRepository repository, EquipListContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getEquipInfo(boolean isFocusNow) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getEquipInfo(isFocusNow, new IListCallBack<EquipBean>() {
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
    public void getEquipList(Map<String, Object> map) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getEquipList(map, new IListCallBack<EquipmentBean>() {
            @Override
            public void onSuccess(@NonNull List<EquipmentBean> list) {
                mView.showEquip(list);
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
    public void getMoreEquipList(Map<String, Object> map) {
        mSubscruptions.add(mRepository.getEquipList(map, new IListCallBack<EquipmentBean>() {
            @Override
            public void onSuccess(@NonNull List<EquipmentBean> list) {
                mView.showMoreEquip(list);
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

    @Override
    public void getEquipByTaskId(long taskId) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getEquipByTaskId(taskId, new IListCallBack<EquipBean>() {
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}