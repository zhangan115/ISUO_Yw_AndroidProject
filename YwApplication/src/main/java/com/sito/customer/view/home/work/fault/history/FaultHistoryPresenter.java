package com.sito.customer.view.home.work.fault.history;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.fault.FaultDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 历史故障
 * Created by Administrator on 2017/6/25.
 */
final class FaultHistoryPresenter implements FaultHistoryContract.Presenter {
    private FaultDataSource mRepository;
    private FaultHistoryContract.View mView;
    private CompositeSubscription subscription;

    FaultHistoryPresenter(FaultDataSource repository, FaultHistoryContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getFaultList(int count) {
        mView.showLoading();
        subscription.add(mRepository.getHistoryList(count, new IListCallBack<FaultList>() {
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
        subscription.add(mRepository.getMoreHistoryList(count, lastId, new IListCallBack<FaultList>() {
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
