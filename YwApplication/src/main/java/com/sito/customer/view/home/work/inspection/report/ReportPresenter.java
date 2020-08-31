package com.sito.customer.view.home.work.inspection.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.db.RoomDb;
import com.sito.customer.mode.bean.inspection.InspectionDetailBean;
import com.sito.customer.mode.bean.inspection.RoomListBean;
import com.sito.customer.mode.bean.inspection.TaskEquipmentBean;
import com.sito.customer.mode.inspection.InspectionSourceData;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 数据录入界面
 * Created by zhangan on 2017-06-26.
 */

class ReportPresenter implements ReportContract.Presenter {


    private final InspectionSourceData mRepository;
    private final ReportContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    ReportPresenter(InspectionSourceData mRepository, ReportContract.View mView) {
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

    private boolean canUpload = true;

    @Override
    public void uploadData(final int position, final InspectionDetailBean inspectionDetailBean, final boolean isAuto) {
        canUpload = false;
        Subscription uploadSub = mRepository.uploadRoomListData(position, inspectionDetailBean
                , new InspectionSourceData.UploadRoomListCallBack() {

                    @Override
                    public void onSuccess() {
                        canUpload = true;
                        if (!isAuto) {
                            mView.hideUploadLoading();
                        }
                        mView.showUploadSuccess(isAuto);
                    }

                    @Override
                    public void noDataUpload() {
                        canUpload = true;
                        if (!isAuto) {
                            mView.hideUploadLoading();
                            mView.noDataUpload();
                        }
                    }

                    @Override
                    public void onError() {
                        canUpload = true;
                        if (!isAuto) {
                            mView.hideUploadLoading();
                            mView.uploadError();
                        }
                    }
                });
        mSubscriptions.add(uploadSub);
    }

    @Override
    public void uploadRandomImage(RoomDb roomDb) {
        mSubscriptions.add(mRepository.uploadRandomDataPhoto(roomDb, new InspectionSourceData.UploadPhotoCallBack() {

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
        mSubscriptions.add(mRepository.loadRoomListDataFromDb(taskId, roomListBean, new InspectionSourceData.LoadRoomListDataCallBack() {
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
        mRepository.uploadPhotoList(roomDb, new InspectionSourceData.IUploadOfflineCallBack() {
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

    @Override
    public boolean isUploading() {
        return canUpload;
    }

    @Override
    public void saveEditTaskEquipToCache(TaskEquipmentBean taskEquipmentBean) {
        mRepository.saveTaskEquipToRepository(taskEquipmentBean);
    }

    @Nullable
    @Override
    public TaskEquipmentBean getTaskEquipFromRepository() {
        return mRepository.getTskEquipFromRepository();
    }

    @Nullable
    @Override
    public InspectionDetailBean getInspectionData() {
        return mRepository.getInspectionDataFromCache();
    }
}
