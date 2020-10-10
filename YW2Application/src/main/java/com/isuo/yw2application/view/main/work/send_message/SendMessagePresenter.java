package com.isuo.yw2application.view.main.work.send_message;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 发送信息
 * Created by zhangan on 2018/3/6.
 */

class SendMessagePresenter implements SendMessageContract.Presenter {
    private final CustomerRepository mRepository;
    private final SendMessageContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    SendMessagePresenter(CustomerRepository mRepository, SendMessageContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        mView.showUploadProgress();
        subscription.add(mRepository.sendSystemMessage(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.sendSuccess();
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideUploadProgress();
            }
        }));
    }

    @Override
    public void uploadFile(List<String> fileList) {
        mView.showUploadProgress();
        subscription.add(mRepository.uploadFile(fileList, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadFileSuccess(s);
            }

            @Override
            public void onError(String message) {
                mView.uploadFileFail();
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
        subscription.clear();
    }
}
