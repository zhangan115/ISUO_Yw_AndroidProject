package com.sito.evpro.inspection.view.regist;

import android.support.annotation.NonNull;


import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.Register;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/2.
 */
final class RegisterPresenter implements RegisterContract.Presenter {
    private InspectionRepository mRepository;
    private RegisterContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    RegisterPresenter(InspectionRepository repository, RegisterContract.View view) {
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
        mSubscription.add(mRepository.getCode(phoneNum, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.getSuccess();
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
    public void toRegister(@NonNull Register register) {
        mSubscription.add(mRepository.resetPwd(register.getPhoneNum(), register.getPassword(), register.getVerificationCode(), new IObjectCallBack<String>() {
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
