package com.sito.evpro.inspection.view.repair.inspection.detail;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkDataSource;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-26.
 */

class InspectionDetailPresenter implements InspectionDetailContract.Presenter {

    private final InspectionWorkRepository mRepository;
    private final InspectionDetailContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InspectionDetailPresenter(InspectionWorkRepository mRepository, InspectionDetailContract.View mView) {
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
    public void getInspectionDetailList(@NonNull String taskId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionDetailList(taskId, new IObjectCallBack<InspectionDetailBean>() {
            @Override
            public void onSuccess(@NonNull InspectionDetailBean list) {
                mView.showData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void saveEmployee(long taskId, @NonNull List<EmployeeBean> employeeBeen) {
        mSubscriptions.add(mRepository.saveTaskUserToDb(taskId, employeeBeen));
    }

    @Override
    public void loadTaskUserFromDb(long taskId) {
        mSubscriptions.add(mRepository.loadTaskUserFromDb(taskId, new InspectionWorkDataSource.LoadTaskUserCallBack() {
            @Override
            public void onSuccess(@NonNull List<TaskDb> taskDb) {
                mView.showTaskUser(taskDb);
            }

            @Override
            public void onError() {

            }
        }));
    }

    @Override
    public void changeRoomState(long taskId, RoomListBean roomListBean) {
        mSubscriptions.add(mRepository.saveRoomDataToDb(taskId, roomListBean, new InspectionWorkDataSource.SaveRoomDbCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        }));
    }

    @Override
    public void refreshRoomData(long taskId, RoomDb roomDb) {
        mSubscriptions.add(mRepository.refreshRoomData(taskId, roomDb, new InspectionWorkDataSource.RefreshRoomDataCallBack() {
            @Override
            public void onSuccess(RoomDb roomDb) {
                mView.refreshRoomData(roomDb);
            }

            @Override
            public void onError() {

            }
        }));
    }

    @Override
    public void updateRoomState(long taskId, final long taskRoomId, int operation) {
        mSubscriptions.add(mRepository.updateRoomState(taskId, taskRoomId, operation, new IObjectCallBack<String>() {

            @Override
            public void onSuccess(@NonNull String s) {
                mView.updateRoomStateSuccess(taskRoomId);
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
    public void updateTaskAll(long taskId, int operation, String userIds) {
        mSubscriptions.add(mRepository.updateTaskAll(taskId, operation, userIds, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.finishAllRoom();
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
    public boolean checkEquipUploadState(long taskId, long roomId) {
        return mRepository.roomUploadState(taskId, roomId);
    }
}
