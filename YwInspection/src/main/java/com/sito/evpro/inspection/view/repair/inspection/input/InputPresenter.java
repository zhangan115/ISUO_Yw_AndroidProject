package com.sito.evpro.inspection.view.repair.inspection.input;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkDataSource;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepository;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 录入界面
 * Created by zhangan on 2018/2/27.
 */

class InputPresenter implements InputContract.Presenter {

    private final InspectionWorkRepository mRepository;
    private final InputContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Subscription autoSaveData;

    InputPresenter(InspectionWorkRepository mRepository, InputContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void uploadImage(DataItemBean dataItemBean) {
        mSubscriptions.add(mRepository.uploadInspectionPhoto(dataItemBean, new InspectionWorkDataSource.UploadPhotoCallBack() {

            @Override
            public void onSuccess() {
                mView.uploadPhotoSuccess();
            }

            @Override
            public void onFail() {
                mView.uploadPhotoFail();
            }

            @Override
            public void onFinish() {
            }
        }));
    }

    @Override
    public void saveData(TaskEquipmentBean taskEquipmentBean, boolean isAuto) {
        mRepository.saveInputData(taskEquipmentBean, isAuto);
    }

    @Override
    public void startAutoSave(final TaskEquipmentBean taskEquipmentBean) {
        if (autoSaveData == null || autoSaveData.isUnsubscribed()) {
            autoSaveData = Observable.interval(10, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Long aLong) {
                            saveData(taskEquipmentBean, true);
                        }
                    });
        }
        mSubscriptions.add(autoSaveData);
    }

    @Override
    public void stopAutoSave() {
        if (autoSaveData != null && !autoSaveData.isUnsubscribed()) {
            autoSaveData.unsubscribe();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
