package com.isuo.yw2application.view.main.alarm.fault.history;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.fault.FaultDataSource;

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
    public void getFaultList(int userId, int count) {
        mView.showLoading();
        subscription.add(mRepository.getHistoryList(userId,count, new IListCallBack<FaultList>() {
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
    public void getMoreFaultList(int userId, int count, long lastId) {
        subscription.add(mRepository.getMoreHistoryList(userId,count, lastId, new IListCallBack<FaultList>() {
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
