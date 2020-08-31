package com.sito.customer.view.home.work.today.fault;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.work.WorkDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class TodayFaultPresenter implements TodayFaultContract.Presenter {
    private final WorkDataSource mWorkDataSource;
    private final TodayFaultContract.View mView;
    private CompositeSubscription mSubscription = new CompositeSubscription();

    TodayFaultPresenter(WorkDataSource mWorkDataSource, TodayFaultContract.View mView) {
        this.mWorkDataSource = mWorkDataSource;
        this.mView = mView;
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
    public void getData(String time) {
        mView.showLoading();
        mSubscription.add(mWorkDataSource.getTodayFaultList(false, time, new IListCallBack<FaultList>() {
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
}
