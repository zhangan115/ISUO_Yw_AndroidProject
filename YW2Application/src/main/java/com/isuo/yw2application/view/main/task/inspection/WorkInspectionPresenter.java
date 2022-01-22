package com.isuo.yw2application.view.main.task.inspection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.db.RoomDbDao;
import com.isuo.yw2application.mode.bean.inspection.InspectionBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.mode.inspection.InspectionSourceData;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检界面
 * Created by zhangan on 2017-06-22.
 */

public class WorkInspectionPresenter implements WorkInspectionContract.Presenter {

    private final InspectionSourceData mSourceData;
    private final WorkInspectionContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    WorkInspectionPresenter(InspectionRepository mSourceData, WorkInspectionContract.View mView) {
        this.mSourceData = mSourceData;
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
    public void getDataFromAcCache(int inspectionType, String time) {
        mView.showLoading();
        mSubscriptions.add(mSourceData.getInspectionDataFromCache(inspectionType, time, new IListCallBack<InspectionBean>() {
            @Override
            public void onSuccess(@NonNull List<InspectionBean> list) {
                mView.hideLoading();
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
    public void toSaveInspectionDataToAcCache(int inspection, String time, List<InspectionBean> inspectionBeanList) {
        mSubscriptions.add(mSourceData.saveInspectionDataToCache(inspection, time, inspectionBeanList));
    }

    @Override
    public void getData(int inspectionType, @NonNull String time) {
        mView.showLoading();
        mSubscriptions.add(mSourceData.getInspectionData(inspectionType, time, null, new IListCallBack<InspectionBean>() {
            @Override
            public void onSuccess(@NonNull List<InspectionBean> list) {
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
    public void operationTask(String taskId, final InspectionBean bean) {
        mSubscriptions.add(mSourceData.getOperationTask(taskId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                getInspectionDetailDataList(bean);
            }

            @Override
            public void onError(String message) {
                mView.showLoading();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getInspectionDetailDataList(InspectionBean bean) {
        mSubscriptions.add(mSourceData.getInspectionDetailList(bean.getTaskId(), new IObjectCallBack<InspectionDetailBean>() {
            @Override
            public void onSuccess(@NonNull InspectionDetailBean inspectionDetailBean) {
                mView.operationSuccess(bean);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public List<InspectionBean> getUploadTask(List<InspectionBean> list) {
        List<InspectionBean> uploadTask = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTaskState() == ConstantInt.TASK_STATE_3) {
                long taskId = list.get(i).getTaskId();
                InspectionDetailBean detailBean = mSourceData.getInspectionDataFromAcCache(taskId);
                if (detailBean == null) {
                    continue;
                }
                int count = 0;
                for (int j = 0; j < detailBean.getRoomList().size(); j++) {
                    RoomListBean roomListBean = detailBean.getRoomList().get(j);
                    if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_3) {
                        count++;
                    }
                }
                if (count == detailBean.getRoomList().size()) {
                    uploadTask.add(list.get(i));
                }
            }
        }
        return uploadTask;
    }

    @Override
    public void uploadTaskData(InspectionBean task, RoomListBean roomListBean) {
        mSourceData.startUploadTask(task, roomListBean, new InspectionSourceData.UploadTaskCallBack() {

            @Override
            public void onSuccess(@Nullable List<User> users) {
                task.setTaskState(ConstantInt.TASK_STATE_4);
                task.setUsers(users);
                task.setEndTime(System.currentTimeMillis());
                task.setUploadCount(task.getCount());
                mView.uploadNext();
            }

            @Override
            public void noDataUpload() {
                mView.hideLoading();
            }

            @Override
            public void onError() {
                mView.hideLoading();
            }
        });
    }
}
