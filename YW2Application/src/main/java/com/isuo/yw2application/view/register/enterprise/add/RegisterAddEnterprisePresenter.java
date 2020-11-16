package com.isuo.yw2application.view.register.enterprise.add;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.EnterpriseCustomer;
import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.umeng.commonsdk.debug.I;

import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

public class RegisterAddEnterprisePresenter implements RegisterAddEnterpriseContract.Presenter {

    private CustomerRepository mRepository;
    private RegisterAddEnterpriseContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    RegisterAddEnterprisePresenter(CustomerRepository repository, RegisterAddEnterpriseContract.View view) {
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
    public void getEnterpriseCustomerList(JSONObject json) {
        mView.showLoading();
        mSubscription.add(mRepository.getCustomerList(json, new IObjectCallBack<EnterpriseCustomer>() {
            @Override
            public void onSuccess(@NonNull EnterpriseCustomer list) {
                mView.showData(list.getList());
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void askCustomer(JSONObject jsonObject) {
        mSubscription.add(mRepository.joinCustomer(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.joinAskSuccess();
            }

            @Override
            public void onError(String message) {
                mView.joinAskError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
