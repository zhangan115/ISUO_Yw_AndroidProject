package com.isuo.yw2application.view.contact.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.umeng.commonsdk.debug.I;

import org.json.JSONException;
import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private UserDetailContract.View mView;
    private CustomerRepository mRepository;
    private CompositeSubscription mSubscriptions;

    public UserDetailPresenter(UserDetailContract.View mView, CustomerRepository mRepository) {
        this.mView = mView;
        this.mRepository = mRepository;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getUserInfo(int userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscriptions.add(mRepository.getUserInfo(jsonObject, new IObjectCallBack<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                mView.showUserInfo(user);
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
    public void saveUserInfo(JSONObject jsonObject) {
        mSubscriptions.add(mRepository.saveUserInfo(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.saveSuccess();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
