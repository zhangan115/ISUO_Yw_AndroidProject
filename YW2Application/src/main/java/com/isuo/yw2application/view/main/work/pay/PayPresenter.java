package com.isuo.yw2application.view.main.work.pay;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

public class PayPresenter implements PayContract.Presenter {

    private CustomerRepository mRepository;
    private PayContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    public PayPresenter(CustomerRepository mRepository, PayContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        subscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getPayList(JSONObject jsonObject) {

    }

    @Override
    public void pay(JSONObject jsonObject) {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}
