package com.isuo.yw2application.view.main.work.message.join;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.JoinBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class JoinEnterprisePresenter implements JoinEnterpriseContract.Presenter {

    private CustomerRepository mRepository;
    private JoinEnterpriseContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    JoinEnterprisePresenter(CustomerRepository mRepository, JoinEnterpriseContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getPlayJoinList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("passState","0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscription.add(mRepository.getJoinList(jsonObject, new IObjectCallBack<JoinBean>() {
            @Override
            public void onSuccess(@NonNull JoinBean list) {
                mView.showPlayJoinList(list.getList());
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
    public void agreeJoin(long id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscription.add(mRepository.joinAgree(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.agreeSuccess();
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
