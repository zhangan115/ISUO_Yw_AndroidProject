package com.sito.evpro.inspection.view.repair.increment.work;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.Voice;
import com.sito.evpro.inspection.mode.commitinfo.CommitDataSource;
import com.sito.evpro.inspection.mode.commitinfo.CommitRepository;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */

class IncrementWorkPresenter implements IncrementWorkContract.Presenter {

    private final CommitRepository mRepository;
    private final IncrementWorkContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @Inject
    IncrementWorkPresenter(CommitRepository mRepository, IncrementWorkContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
    }


    @Inject
    void setupListeners() {
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
    public void loadDataFromDb(long workId) {
        mRepository.loadIncrementFromDb(workId, new CommitDataSource.LoadIncrementDataCallBack() {
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
}
