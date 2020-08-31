package com.sito.evpro.inspection.view.repair.inspection.report;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkDataSource;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 数据录入界面
 * Created by zhangan on 2017-06-26.
 */

class ReportPresenter implements ReportContract.Presenter {


    private final InspectionWorkRepository mRepository;
    private final ReportContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Subscription autoUpload;

    ReportPresenter(InspectionWorkRepository mRepository, ReportContract.View mView) {
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
    public void startAutoUpload(final int position, final List<TaskEquipmentBean> taskEquipmentBeans) {
        if (autoUpload == null || autoUpload.isUnsubscribed()) {
            autoUpload = Observable.interval(30, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (mView.canUpload()) {
                                uploadData(position, taskEquipmentBeans, true);
                            }
                        }
                    });
        }
        mSubscriptions.add(autoUpload);
    }

    @Override
    public void uploadData(final int position, final List<TaskEquipmentBean> taskEquipmentBeans, final boolean isAuto) {
        if (autoUpload != null) {
            autoUpload.unsubscribe();
        }
        Subscription uploadSub = mRepository.uploadRoomListData(position, taskEquipmentBeans, new InspectionWorkDataSource.UploadRoomListCallBack() {

            @Override
            public void onSuccess() {
                startAutoUpload(position, taskEquipmentBeans);
                if (!isAuto) {
                    mView.hideUploadLoading();
                }
                mView.showUploadSuccess(isAuto);
            }

            @Override
            public void noDataUpload() {
                startAutoUpload(position, taskEquipmentBeans);
                if (!isAuto) {
                    mView.hideUploadLoading();
                    mView.noDataUpload();
                }
            }

            @Override
            public void onError() {
                startAutoUpload(position, taskEquipmentBeans);
                if (!isAuto) {
                    mView.hideUploadLoading();
                    mView.uploadError();
                }
            }
        });
        if (!isAuto) {
            mSubscriptions.add(uploadSub);
        }
    }

    @Override
    public void uploadRandomImage(RoomDb roomDb) {
        mSubscriptions.add(mRepository.uploadRandomDataPhoto(roomDb, new InspectionWorkDataSource.UploadPhotoCallBack() {

            @Override
            public void onSuccess() {
                mView.uploadRandomSuccess();
            }

            @Override
            public void onFail() {
                mView.uploadRandomFail();
            }

            @Override
            public void onFinish() {
            }
        }));
    }

    @Override
    public void loadInspectionDataFromDb(long taskId, @NonNull RoomListBean roomListBean) {
        mView.showLoading();
        mSubscriptions.add(mRepository.loadRoomListDataFromDb(taskId, roomListBean, new InspectionWorkDataSource.LoadRoomListDataCallBack() {
            @Override
            public void onSuccess(RoomListBean roomListBean) {
                mView.hideLoading();
                mView.showData(roomListBean);
            }

            @Override
            public void onError() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void uploadUserPhoto(long taskId, long equipmentId, String url) {
        mSubscriptions.add(mRepository.uploadUserPhotoInfo(taskId, equipmentId, url, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadUserPhotoSuccess();
            }

            @Override
            public void onError(String message) {
                mView.uploadUserPhotoFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public boolean checkPhotoNeedUpload(List<TaskEquipmentBean> taskEquipmentBeans) {
        return mRepository.checkPhotoInspectionData(taskEquipmentBeans);
    }

    @Override
    public void uploadPhotoList(RoomDb roomDb) {
        mRepository.uploadPhotoList(roomDb, new InspectionWorkDataSource.IUploadOfflineCallBack() {
            @Override
            public void onFinish() {
                mView.uploadOfflinePhotoFinish();
            }

            @Override
            public void onFail() {
                mView.hideUploadLoading();
                mView.uploadOfflinePhotoFail();
            }
        });
    }
}
