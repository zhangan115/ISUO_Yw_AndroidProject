package com.isuo.yw2application.view.main.task.inspection.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.inspection.InspectionBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.inspection.InspectionSourceData;

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


    @Override
    public void loadInspectionDataFromDb(long taskId, @NonNull RoomListBean roomListBean) {
        mSubscriptions.add(mRepository.loadRoomListDataFromDb(taskId, roomListBean, new InspectionSourceData.LoadRoomListDataCallBack() {
            @Override
            public void onSuccess(RoomListBean roomListBean) {
                mView.showData(roomListBean);
            }

            @Override
            public void onError() {

            }
        }));
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
    public InspectionDetailBean getInspectionData(long taskId) {
        return mRepository.getInspectionDataFromAcCache(taskId);
    }

    @Override
    public int getEquipmentFinishCount(long taskId, @NonNull RoomListBean roomListBean) {
        return mRepository.getEquipmentFinishCount(taskId, roomListBean);
    }

    @Override
    public long getEquipmentDataFinishCount(long taskId, long roomId, long equipmentId) {
        return mRepository.getEquipmentInputCount(taskId, roomId, equipmentId);
    }

    @Override
    public boolean getEquipmentFinishState(long taskId, long roomId, long equipmentId) {
        return mRepository.getEquipmentFinishState(taskId, roomId, equipmentId);
    }

    @Override
    public void uploadTaskData(InspectionDetailBean task, RoomListBean roomListBean) {
        mView.showUploadLoading();
        mRepository.startRoomUploadTask(task, roomListBean, new InspectionSourceData.UploadTaskCallBack() {

            @Override
            public void onSuccess(@Nullable List<User> users) {
                mView.uploadDataSuccess(users);
                mView.hideUploadLoading();
            }

            @Override
            public void noDataUpload() {
                mView.hideUploadLoading();
            }

            @Override
            public void onError() {
                mView.hideUploadLoading();
                mView.uploadDataError();
            }
        });
    }


}
