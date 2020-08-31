package com.sito.customer.view.home.work.today.doing;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.today.TodayToDoBean;
import com.sito.customer.mode.work.WorkDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

class TodayDoingPresenter implements TodayDoingContact.Presenter {

    private final WorkDataSource mWorkDataSource;
    private final TodayDoingContact.View mView;
    private CompositeSubscription mSubscription = new CompositeSubscription();

    TodayDoingPresenter(WorkDataSource mWorkDataSource, TodayDoingContact.View mView) {
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
    public void getData() {
        mView.showLoading();
        mSubscription.add(mWorkDataSource.getTodayToList(new IListCallBack<TodayToDoBean>() {

            @Override
            public void onSuccess(@NonNull List<TodayToDoBean> list) {
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
