package com.isuo.yw2application.view.main.task.inspection.work;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.inspection.InspectionSourceData;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检 room list
 * Created by zhangan on 2018/3/20.
 */

class InspectionRoomPresenter implements InspectionRoomContract.Presenter {

    private final InspectionRoomContract.View mView;
    private final InspectionSourceData mSourceData;
    private CompositeSubscription mSubscription;

    InspectionRoomPresenter(InspectionRoomContract.View mView, InspectionSourceData mSourceData) {
        this.mView = mView;
        this.mSourceData = mSourceData;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getInspectionDataList(long taskId) {
        mView.showLoading();
        mSubscription.add(mSourceData.getInspectionDetailList(taskId, new IObjectCallBack<InspectionDetailBean>() {
            @Override
            public void onSuccess(@NonNull InspectionDetailBean inspectionDetailBean) {
                mView.showData(inspectionDetailBean);
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }


    @Override
    public void loadTaskUserFromDb(long taskId) {
        mSubscription.add(mSourceData.loadTaskUserFromDb(taskId, new InspectionSourceData.LoadTaskUserCallBack() {
            @Override
            public void onSuccess(@NonNull ArrayList<TaskDb> taskDb) {
                mView.showTaskUser(taskDb);
            }

            @Override
            public void onError() {

            }
        }));
    }

    @Override
    public void saveEmployee(long taskId, @NonNull List<EmployeeBean> taskDbList) {
        mSubscription.add(mSourceData.saveTaskUserToDb(taskId, taskDbList));
    }

    @Override
    public void updateRoomState(long taskId, RoomListBean roomListBean, int operation) {
        mSubscription.add(mSourceData.updateRoomState(taskId, roomListBean, operation, new IObjectCallBack<String>() {

            @Override
            public void onSuccess(@NonNull String s) {
                mView.updateRoomStateSuccess();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void roomListFinish(long taskId, int operation, String userIds) {
        mSubscription.add(mSourceData.roomListFinish(taskId, operation, userIds, new IObjectCallBack<String>() {

            @Override
            public void onSuccess(@NonNull String s) {
                mView.finishAllRoom();
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
    public void loadRoomDataFromDb(long taskId, List<RoomListBean> list) {
        mSubscription.add(mSourceData.loadRoomDataFromDb(taskId, list, new InspectionSourceData.LoadRoomDataCallBack() {
            @Override
            public void onSuccess(List<RoomListBean> list) {
                mView.showData();
            }

            @Override
            public void onError() {

            }
        }));
    }

    @Override
    public InspectionDetailBean getInspectionFromCache() {
        return mSourceData.getInspectionDataFromCache();
    }

    @Override
    public void saveInspectionToCache(@Nullable InspectionDetailBean bean) {
        mSourceData.saveInspectionDataToCache(bean);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
