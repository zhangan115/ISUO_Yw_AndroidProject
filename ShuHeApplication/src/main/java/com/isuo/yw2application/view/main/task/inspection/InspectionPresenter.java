package com.isuo.yw2application.view.main.task.inspection;

import android.support.annotation.NonNull;

import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.inspection.InspectionRepository;
import com.isuo.yw2application.mode.inspection.InspectionSourceData;
import com.isuo.yw2application.mode.work.WorkRepository;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检界面
 * Created by zhangan on 2017-06-22.
 */

public class InspectionPresenter implements InspectionContract.Presenter {

    private final WorkRepository mRepository;
    private final InspectionRepository inspectionRepository;
    private final InspectionContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InspectionPresenter(WorkRepository mRepository, InspectionRepository inspectionRepository, InspectionContract.View mView) {
        this.mRepository = mRepository;
        this.inspectionRepository = inspectionRepository;
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
    public void getDataFromCache(int inspection, String time) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionDataFromCache(inspection, time, new IListCallBack<InspectionBean>() {
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
    public void toSaveInspectionDataToCache(int inspection, String time, List<InspectionBean> inspectionBeanList) {
        mRepository.saveInspectionDataToCache(inspection, time, inspectionBeanList);
    }

    @Override
    public void getData(int inspectionType, @NonNull String time) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionData(inspectionType, time, null, new IListCallBack<InspectionBean>() {
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
        mSubscriptions.add(mRepository.getOperationTask(taskId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                getInspectionDataList(bean);
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
    public void getInspectionDataList(InspectionBean bean) {
        mSubscriptions.add(mRepository.getInspectionDetailList(bean.getTaskId(), new IObjectCallBack<InspectionDetailBean>() {
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
            if (list.get(i).getTaskState() != ConstantInt.TASK_STATE_3) {
                continue;
            }
            long taskId = list.get(i).getTaskId();
            InspectionDetailBean detailBean = inspectionRepository.getInspectionDataFromAcCache(taskId);
            if (detailBean == null) {
                continue;
            }
            for (int j = 0; j < detailBean.getRoomList().size(); j++) {
                RoomListBean roomListBean = detailBean.getRoomList().get(j);
                int count = inspectionRepository.getEquipmentFinishPutCount(taskId, roomListBean);
                if (roomListBean.getTaskRoomState() == ConstantInt.ROOM_STATE_2
                        && count == roomListBean.getTaskEquipment().size()) {
                    uploadTask.add(list.get(i));
                }
            }
        }
        return uploadTask;
    }

    @Override
    public void uploadTaskData(InspectionBean task) {
        mSubscriptions.add(inspectionRepository.uploadTaskData(task, new InspectionSourceData.UploadTaskCallBack() {
            @Override
            public void onSuccess() {
                task.setTaskState(ConstantInt.TASK_STATE_4);
                task.setEndTime(System.currentTimeMillis());
                task.setCount(task.getCount());
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
        }));
    }
}
