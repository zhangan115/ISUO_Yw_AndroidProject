package com.sito.evpro.inspection.view.increment;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;
import com.sito.evpro.inspection.mode.commitinfo.CommitRepository;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 增值工作
 * Created by Administrator on 2017/6/25.
 */
final class IncrementPresenter implements IncrementContract.Presenter {
    private CommitRepository mRepository;
    private IncrementContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    IncrementPresenter(CommitRepository repository, IncrementContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        subscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
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
