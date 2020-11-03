package com.isuo.yw2application.view.main.task.inspection.input;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.inspection.InspectionSourceData;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 录入界面
 * Created by zhangan on 2018/2/27.
 */

class InputPresenter implements InputContract.Presenter {

    private final InspectionSourceData mRepository;
    private final InputContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InputPresenter(InspectionSourceData mRepository, InputContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void saveTaskEquipToCache(TaskEquipmentBean taskEquipmentBean) {
        mRepository.saveTaskEquipToRepository(taskEquipmentBean);
    }

    @Override
    public void saveData(TaskEquipmentBean taskEquipmentBean, boolean isAuto) {
        mRepository.saveInputData(taskEquipmentBean, isAuto);
    }

    @Nullable
    @Override
    public InspectionDetailBean getInspectionData() {
        return mRepository.getInspectionDataFromCache();
    }

    @Override
    public void getEquipmentCare(long equipmentId) {
        mSubscriptions.add(mRepository.getCareEquipmentData(equipmentId, new IObjectCallBack<FocusBean>() {
            @Override
            public void onSuccess(@NonNull FocusBean s) {
                mView.showCareData(s);
            }

            @Override
            public void onError(String message) {
                mView.showCareDataFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getShareData(TaskEquipmentBean taskEquipmentBean) {
        mSubscriptions.add(mRepository.getShareData(taskEquipmentBean, new InspectionSourceData.IShareDataCallBack() {
            @Override
            public void onSuccess() {
                mView.showTaskEquipmentData();
            }

            @Override
            public void onFail() {

            }
        }));
    }

    @Override
    public void getTaskEquipFromCache() {
        mSubscriptions.add(mRepository.getTaskEquipmentData(new InspectionSourceData.IGetTaskEquipmentCallBack() {
            @Override
            public void onGetData(TaskEquipmentBean taskEquipmentBean) {
                mView.showTaskEquipFromCache(taskEquipmentBean);
            }

            @Override
            public void noData() {

            }
        }));
    }

    @Override
    public void uploadImage(DataItemBean dataItemBean) {
        mSubscriptions.add(mRepository.uploadInspectionPhoto(dataItemBean
                , new InspectionSourceData.UploadPhotoCallBack() {

                    @Override
                    public void onSuccess() {
                        mView.uploadItemPhotoFinish(true);
                    }

                    @Override
                    public void onFail() {
                        mView.uploadItemPhotoFinish(false);
                    }

                    @Override
                    public void onFinish() {

                    }
                }));
    }

    @Override
    public void uploadEquipmentPhotoImage(RoomDb roomDb) {
        mSubscriptions.add(mRepository.uploadRandomDataPhoto(roomDb, new InspectionSourceData.UploadPhotoCallBack() {

            @Override
            public void onSuccess() {
                mView.uploadREquipmentPhotoFinish(true);
            }

            @Override
            public void onFail() {
                mView.uploadREquipmentPhotoFinish(false);
            }

            @Override
            public void onFinish() {
            }
        }));
    }

    @Override
    public void uploadEquipmentInfo(long taskId, long equipmentId, String url) {
        mSubscriptions.add(mRepository.uploadUserPhotoInfo(taskId, equipmentId, url, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadEquipmentInfoFinish(true);
            }

            @Override
            public void onError(String message) {
                mView.uploadEquipmentInfoFinish(false);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    private boolean canUpload = true;

    @Override
    public void uploadTaskData(int position, InspectionDetailBean inspectionDetailBean, TaskEquipmentBean equipmentBean) {
        canUpload = false;
        Subscription uploadSub = mRepository.uploadTaskEquipmentListData(position, inspectionDetailBean,equipmentBean
                , new InspectionSourceData.UploadRoomListCallBack() {

                    @Override
                    public void onSuccess() {
                        canUpload = true;
                        mView.hideUploadLoading();
                        mView.uploadDataSuccess();
                    }

                    @Override
                    public void noDataUpload() {
                        canUpload = true;
                        mView.hideUploadLoading();

                    }

                    @Override
                    public void onError() {
                        canUpload = true;
                        mView.hideUploadLoading();
                        mView.uploadDataError();
                    }
                });
        mSubscriptions.add(uploadSub);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
