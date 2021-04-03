package com.isuo.yw2application.view.main.task.increment.execute;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.increment.IncrementDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

class IncrementWorkPresenter implements IncrementWorkContract.Presenter {

    private final IncrementDataSource mRepository;
    private final IncrementWorkContract.View mView;
    private CompositeSubscription mSubscriptions;

    IncrementWorkPresenter(IncrementDataSource mRepository, IncrementWorkContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
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
    public void loadDataFromDb(long workId) {
        mRepository.loadIncrementFromDb(workId, new IncrementDataSource.LoadIncrementDataCallBack() {
            @Override
            public void onSuccess(List<Image> imageList, Voice voice) {
                mView.showDataFromDb(imageList, voice);
            }
        });
    }

    @Override
    public void postVoiceFile(String businessType, Voice voice) {
        mSubscriptions.add(mRepository.postIncrementVoiceFile(voice, businessType, new IListCallBack<String>() {
            @Override
            public void onSuccess(@NonNull List<String> list) {
                mView.uploadVoiceSuccess(list.get(0));
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void saveVoiceToDb(Voice voice) {
        mRepository.saveVoice(voice);
    }

    @Override
    public void uploadIncrementInfo(String jsonStr) {
        mSubscriptions.add(mRepository.uploadIncrementData(jsonStr, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadAllDataSuccess();
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void cleanAllData(List<Image> imageList, Voice voice) {
        mRepository.cleanImageData(imageList);
        mRepository.cleanVoiceData(voice);
    }

    @Override
    public void uploadImage(int workType, long workId, String businessType, Image image) {
        mSubscriptions.add(mRepository.uploadImageFile(workType, businessType, workId, image, new UploadImageCallBack() {
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
    public void startIncrementWork(long workId) {
        mSubscriptions.add(mRepository.startIncrement(workId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.startSuccess();
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
