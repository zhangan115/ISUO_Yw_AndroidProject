package com.sito.evpro.inspection.view.increment.history;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.increment.IncreList;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/6 18:04
 * E-mail：yangzongbin@si-top.com
 */
final class IncreHistoryPresenter implements IncreHistoryContract.Presenter {
    private InspectionRepository mRepository;
    private IncreHistoryContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    IncreHistoryPresenter(InspectionRepository repository, IncreHistoryContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getIncreList(int count) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getIncrementHistoryList(count, new IListCallBack<IncrementBean>() {
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
    public void getMoreIncreList(int count, long lastId) {
        mSubscruptions.add(mRepository.getIncrementMoreHistoryList(count, lastId, new IListCallBack<IncrementBean>() {
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}