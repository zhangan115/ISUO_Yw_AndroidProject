package com.sito.customer.view.home.work.inject.oil;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.inject.InjectOilDataSource;
import com.sito.customer.mode.inject.bean.InjectItemBean;

import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

/**
 * 注油
 * Created by zhangan on 2018/4/12.
 */

class InjectOilAPresenter implements InjectOilContract.Presenter {

    private final InjectOilContract.View mView;
    private final InjectOilDataSource mDataSource;
    private CompositeSubscription subscription;

    InjectOilAPresenter(InjectOilContract.View mView, InjectOilDataSource mDataSource) {
        this.mView = mView;
        this.mDataSource = mDataSource;
        mView.setPresenter(this);
    }

    @Override
    public void getInjectList() {
        subscription.add(mDataSource.getInjectItem(new IObjectCallBack<InjectItemBean>() {

            @Override
            public void onSuccess(@NonNull InjectItemBean injectItemBean) {
                mView.showInjectItem(injectItemBean);
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
    public void injectOilEquipment(JSONObject jsonObject) {
        subscription.add(mDataSource.injectOilEquipment(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.injectSuccess();
            }

            @Override
            public void onError(String message) {
                mView.injectFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void subscribe() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}
