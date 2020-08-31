package com.sito.customer.view.login;

import android.support.annotation.NonNull;

import com.sito.customer.app.CustomerApp;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.customer.CustomerDataSource;
import com.sito.customer.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;


/**
 * (P层)登陆
 * Created by zhangan on 2016-12-06.
 */

final class LoginPresenter implements LoginContract.Presenter {

    private CustomerRepository mRepository;
    private LoginContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @Inject
    LoginPresenter(CustomerRepository repository, LoginContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void login(String name, String pass) {
        mView.loginLoading();
        mSubscriptions.add(mRepository.login(name, pass, new CustomerDataSource.LoadUserCallBack() {

            @Override
            public void onLoginSuccess(@NonNull User user) {
                CustomerApp.getInstance().setCurrentUser(user);
                mView.loginSuccess();
            }

            @Override
            public void onLoginFail() {
                mView.loginFail();
            }

            @Override
            public void onFinish() {
                mView.loginHideLoading();
            }

        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
