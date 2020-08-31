package com.sito.customer.view.pass;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.Register;
import com.sito.customer.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/2.
 */
final class ForgePassPresenter implements ForgePassContract.Presenter {
    private CustomerRepository mRepository;
    private ForgePassContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    ForgePassPresenter(CustomerRepository repository, ForgePassContract.View view) {
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
