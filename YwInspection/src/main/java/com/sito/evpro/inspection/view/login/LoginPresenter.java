package com.sito.evpro.inspection.view.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.inspection.InspectionDataSource;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;


/**
 * (P层)登陆
 * Created by zhangan on 2016-12-06.
 */

final class LoginPresenter implements LoginContract.Presenter {

    private InspectionRepository mRepository;
    private LoginContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @Inject
    LoginPresenter(InspectionRepository repository, LoginContract.View view) {
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
        mSubscriptions.add(mRepository.login(name, pass, new InspectionDataSource.LoadUserCallBack() {

            @Override
            public void onLoginSuccess(@NonNull User user) {
                InspectionApp.getInstance().setCurrentUser(user);
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
    public void loadHistoryUser(@NonNull String userName, @Nullable List<User> userList) {

    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
