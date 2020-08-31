package com.sito.evpro.inspection.view.repair.inspection;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.evpro.inspection.mode.bean.inspection.OperationBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-22.
 */

public class InspectionPresenter implements InspectionContract.Presenter {
    private final InspectionRepository mRepository;
    private final InspectionContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public InspectionPresenter(InspectionRepository mRepository, InspectionContract.View mView) {
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
    public void getInspectionList(@NonNull String time) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionList(time, new IListCallBack<InspectionBean>() {
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
    public void getInspectionListMore(@NonNull String time, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getInspectionList(time, lastId, new IListCallBack<InspectionBean>() {
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
        mSubscriptions.add(mRepository.operationTask(taskId, position, new IObjectCallBack<OperationBean>() {
            @Override
            public void onSuccess(@NonNull OperationBean operationBean) {
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
