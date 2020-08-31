package com.sito.evpro.inspection.view.equipment.equiplist;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/18 15:50
 * E-mail：yangzongbin@si-top.com
 */
final class EquipListPresenter implements EquipListContract.Presenter {
    private InspectionRepository mRepository;
    private EquipListContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    EquipListPresenter(InspectionRepository repository, EquipListContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        subscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
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
    public void getEquipList() {
        mView.showLoading();
        subscription.add(mRepository.getEquipInfo(new IListCallBack<EquipBean>() {
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
        subscription.add(mRepository.getEquipList(map, new IListCallBack<EquipmentBean>() {
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
        subscription.add(mRepository.getEquipList(map, new IListCallBack<EquipmentBean>() {
            @Override
            public void onSuccess(@NonNull List<EquipmentBean> list) {
                mView.showMoreEquipment(list);
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
        subscription.add(mRepository.getEquipByTaskId(taskId, new IListCallBack<EquipBean>() {
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
}