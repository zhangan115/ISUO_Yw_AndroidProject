package com.sito.customer.view.home.work.inspection;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.work.InspectionBean;
import com.sito.customer.mode.work.WorkRepository;

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
    public void getDataMore(int inspectionType, @NonNull String time, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getInspectionData(inspectionType, time, lastId, new IListCallBack<InspectionBean>() {
            @Override
            public void onSuccess(@NonNull List<InspectionBean> list) {
                mView.showMoreData(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }

    @Override
    public void operationTask(String taskId, final int position) {
        mSubscriptions.add(mRepository.getOperationTask(taskId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.operationSuccess(position);
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
