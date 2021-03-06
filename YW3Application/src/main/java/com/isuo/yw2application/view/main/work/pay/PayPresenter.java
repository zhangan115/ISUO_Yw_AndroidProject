package com.isuo.yw2application.view.main.work.pay;

import android.support.annotation.NonNull;

import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class PayPresenter implements PayContract.Presenter {

    private CustomerRepository mRepository;
    private PayContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    PayPresenter(CustomerRepository mRepository, PayContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        subscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getPayList(JSONObject jsonObject) {
        subscription.add(mRepository.getMenuList(jsonObject, new IListCallBack<PayMenuBean>() {
            @Override
            public void onSuccess(@NonNull List<PayMenuBean> list) {
                mView.showPayList(list);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void paySuccess(JSONObject jsonObject) {
        subscription.add(mRepository.paySuccessBack(jsonObject, new IObjectCallBack<User>() {
            @Override
            public void onSuccess(@NonNull User s) {
                Yw2Application.getInstance().setCurrentUser(s);
                mView.paySuccess();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getAlPayInfo(JSONObject jsonObject) {
        subscription.add(mRepository.getPayInfoAl(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.payAli(s);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.hidePopView();
            }
        }));
    }

    @Override
    public void getWeiXinPayInfo(JSONObject jsonObject) {
        subscription.add(mRepository.getPayInfo(jsonObject, new IObjectCallBack<WeiXinPayBean>() {
            @Override
            public void onSuccess(@NonNull WeiXinPayBean s) {
                mView.payWeiXin(s);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.hidePopView();
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}
