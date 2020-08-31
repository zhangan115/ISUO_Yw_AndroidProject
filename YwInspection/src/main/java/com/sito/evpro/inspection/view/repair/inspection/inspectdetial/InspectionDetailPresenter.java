package com.sito.evpro.inspection.view.repair.inspection.inspectdetial;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.fault.CheckBean;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/17 19:36
 * E-mail：yangzongbin@si-top.com
 */
final class InspectionDetailPresenter implements InspectionDetailContract.Presenter {
    private InspectionRepository mRepository;
    private InspectionDetailContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    InspectionDetailPresenter(InspectionRepository repository, InspectionDetailContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getCheckInfo(long taskId) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getCheckInfo(taskId, new IObjectCallBack<CheckBean>() {
            @Override
            public void onSuccess(@NonNull CheckBean bean) {
                mView.showCheckInfo(bean);
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
    public void getFaultList(long taskId) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getFaultList(taskId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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
    public void getMoreFaultList(long taskId, int lastId) {
        mSubscruptions.add(mRepository.getMoreFaultList(taskId, lastId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}