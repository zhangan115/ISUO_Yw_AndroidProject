package com.isuo.yw2application.view.main.task.increment.submit;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.increment.IncrementDataSource;

import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 增值工作
 * Created by Administrator on 2017/6/25.
 */
final class IncrementPresenter implements IncrementContract.Presenter {
    private IncrementDataSource mRepository;
    private IncrementContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    IncrementPresenter(IncrementDataSource repository, IncrementContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void postVoiceFile(int workType, String businessType) {
        subscription.add(mRepository.postIncrementVoiceFile(workType, businessType, new IListCallBack<String>() {
            @Override
            public void onSuccess(@NonNull List<String> list) {
                mView.postVoiceSuccess(list);
            }

            @Override
            public void onError(String message) {
                mView.postFail();
            }

            @Override
            public void onFinish() {
                mView.postFinish();
            }
        }));
    }



    @Override
    public void uploadSuccess() {
        mRepository.cleanIncrementCache();
    }

    @Override
    public void uploadImage(int workType, String businessType, Image image) {
        subscription.add(mRepository.uploadImageFile(workType, businessType, null, image, new UploadImageCallBack() {
            @Override
            public void onSuccess() {
                mView.uploadImageSuccess();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError(Image image) {
                mView.uploadImageFail(image);
            }
        }));
    }

    @Override
    public void postIncrementInfo(JSONObject jsonObject) {
        mView.showLoading();
        subscription.add(mRepository.postIncrementInfo(jsonObject, new IObjectCallBack<UploadResult>() {
            @Override
            public void onSuccess(@NonNull UploadResult uploadResult) {
                mView.postSuccess(uploadResult);
            }

            @Override
            public void onError(String message) {
                mView.postFail();
            }

            @Override
            public void onFinish() {
                mView.postFinish();
            }
        }));
    }
}
