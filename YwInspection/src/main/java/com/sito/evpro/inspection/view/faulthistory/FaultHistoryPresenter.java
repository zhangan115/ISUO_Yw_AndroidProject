package com.sito.evpro.inspection.view.faulthistory;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/25.
 */
final class FaultHistoryPresenter implements FaultHistoryContract.Presenter {
    private InspectionRepository mRepository;
    private FaultHistoryContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    FaultHistoryPresenter(InspectionRepository repository, FaultHistoryContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
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
        mSubscruptions.clear();
    }

    @Override
    public void getFaultList(int count) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getHistoryList(count, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
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
    public void getMoreFaultList(int count, long lastId) {
        mSubscruptions.add(mRepository.getMoreHistoryList(count, lastId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
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
