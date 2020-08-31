package com.sito.customer.view.home.discover.faulttime;

import android.support.annotation.NonNull;

import com.sito.customer.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 15:35
 * E-mail：yangzongbin@si-top.com
 */
final class FaultTimePresenter implements FaultTimeContract.Presenter {
    private CustomerRepository mRepository;
    private FaultTimeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    FaultTimePresenter(CustomerRepository repository, FaultTimeContract.View view) {
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
    public void getChartData(String time) {

    }
}