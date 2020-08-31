package com.sito.evpro.inspection.view.repair.increment;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-22.
 */

public class IncrementPresenter implements IncrementContract.Presenter {

    @NonNull
    private CompositeSubscription mSubscriptions;
    private final InspectionRepository mRepository;
    private final IncrementContract.View mView;

    public IncrementPresenter(InspectionRepository mRepository, IncrementContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getIncrementList(@NonNull String time) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getIncrementList(time, new IListCallBack<IncrementBean>() {
            @Override
            public void onSuccess(@NonNull List<IncrementBean> list) {
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
    public void getIncrementListMore(@NonNull String time, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getIncrementList(time, lastId, new IListCallBack<IncrementBean>() {
            @Override
            public void onSuccess(@NonNull List<IncrementBean> list) {
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

    @Override
    public void startIncrement(final int position, long workId) {
        mSubscriptions.add(mRepository.startIncrement(workId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.startSuccess(position);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
