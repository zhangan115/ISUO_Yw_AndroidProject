package com.isuo.yw2application.view.register;

import android.support.annotation.NonNull;

import  com.isuo.yw2application.mode.IObjectCallBack;
import  com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONObject;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/2.
 */
final class RegisterPresenter implements RegisterContract.Presenter {
    private CustomerRepository mRepository;
    private RegisterContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    RegisterPresenter(CustomerRepository repository, RegisterContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getVerificationCode(@NonNull String phoneNum) {
        mSubscription.add(mRepository.getRegisterCode(phoneNum, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.getSuccess(s);
            }

            @Override
            public void onError(String message) {
                mView.getFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }


    @Override
    public void toRegister(@NonNull JSONObject json) {
        mSubscription.add(mRepository.addUserRegister(json, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.registerSuccess();
            }

            @Override
            public void onError(String message) {
                mView.registerFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
