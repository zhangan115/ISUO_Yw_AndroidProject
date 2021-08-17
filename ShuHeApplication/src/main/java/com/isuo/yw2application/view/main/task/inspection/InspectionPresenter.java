package com.isuo.yw2application.view.main.task.inspection;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.work.WorkRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检界面
 * Created by zhangan on 2017-06-22.
 */

public class InspectionPresenter implements InspectionContract.Presenter {

    private final WorkRepository mRepository;
    private final InspectionContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InspectionPresenter(WorkRepository mRepository, InspectionContract.View mView) {
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
                mView.operationSuccess(bean);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
