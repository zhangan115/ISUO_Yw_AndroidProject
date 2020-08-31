package com.sito.customer.view.home.discover.faulttype;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.discover.FaultLevel;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 15:48
 * E-mail：yangzongbin@si-top.com
 */
final class FaultTypePresenter implements FaultTypeContract.Presenter {
    private CustomerRepository mRepository;
    private FaultTypeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    FaultTypePresenter(CustomerRepository repository, FaultTypeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getChartData(String time) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getFaultLevel(time, new IListCallBack<FaultLevel>() {
            @Override
            public void onSuccess(@NonNull List<FaultLevel> list) {
                mView.showChartData(list);
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