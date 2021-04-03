package com.isuo.yw2application.view.register.enterprise.create;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

public class RegisterCreateEnterprisePresenter implements RegisterCreateEnterpriseContract.Presenter {

    private CustomerRepository mRepository;
    private RegisterCreateEnterpriseContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    RegisterCreateEnterprisePresenter(CustomerRepository repository, RegisterCreateEnterpriseContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void createEnterpriseCustomer(JSONObject json) {
        mSubscription.add(mRepository.addCustomer(json, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.createSuccess();
            }

            @Override
            public void onError(String message) {
                mView.createError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
