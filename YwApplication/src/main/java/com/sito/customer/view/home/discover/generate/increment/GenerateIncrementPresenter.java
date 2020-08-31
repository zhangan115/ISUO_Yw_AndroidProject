package com.sito.customer.view.home.discover.generate.increment;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.generate.GenerateRepository;

import org.json.JSONObject;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 生成增值工作
 * Created by zhangan on 2017/9/29.
 */

class GenerateIncrementPresenter implements GenerateIncrementContract.Presenter {

    private final GenerateRepository mRepository;
    private final GenerateIncrementContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    GenerateIncrementPresenter(GenerateRepository mRepository, GenerateIncrementContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
        setUpListeners();
    }

    private void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void uploadImage(String businessType, Image image) {
        mSubscriptions.add(mRepository.uploadImageFile(businessType, image, new UploadImageCallBack() {
            @Override
            public void onSuccess() {
                mView.uploadImgSuccess();
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onError(Image image) {
                mView.uploadImgFail(image);
            }
        }));
    }

    @Override
    public void uploadImage(final int position, String businessType, String path) {
        mSubscriptions.add(mRepository.uploadImageFile(businessType, path, new IObjectCallBack<Image>() {

            @Override
            public void onSuccess(@NonNull Image image) {
                mView.uploadImgSuccess(position, image);
            }

            @Override
            public void onError(String message) {
                mView.showErrorMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void uploadVoiceFile(String businessType, String path) {
        mView.showUploadProgress();
        mSubscriptions.add(mRepository.uploadVoiceFile(businessType, path, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadVoiceSuccess(s);
            }

            @Override
            public void onError(String message) {
                mView.hideUploadProgress();
                mView.showErrorMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void deleteVoiceFile(String voicePath) {
        mRepository.deleteFile(voicePath);
    }

    @Override
    public void uploadAllData(JSONObject uploadJson) {
        mSubscriptions.add(mRepository.uploadIncrementInfo(uploadJson, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadDataSuccess();
            }

            @Override
            public void onError(String message) {
                mView.showErrorMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideUploadProgress();
            }
        }));
    }
}
