package com.isuo.yw2application.view.main.task;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.work.WorkMonitorState;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.isuo.yw2application.mode.work.WorkDataSource;

import rx.subscriptions.CompositeSubscription;

/**
 * 工作
 * Created by Administrator on 2017/6/28.
 */
class TaskPresenter implements TaskContract.Presenter {
    private WorkDataSource mRepository;
    private TaskContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    TaskPresenter(WorkDataSource repository, TaskContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getWorkCount() {
        subscription.add(mRepository.getWorkState(new IObjectCallBack<WorkState>() {
            @Override
            public void onSuccess(@NonNull WorkState workState) {
                mView.showWorkCount(workState);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.showWorkCountFinish();
            }
        }));
    }

    @Override
    public void getWorkMonitorCount() {
        subscription.add(mRepository.getWorkMonitorState(new IObjectCallBack<WorkMonitorState>() {
            @Override
            public void onSuccess(@NonNull WorkMonitorState workState) {
                mView.showWorkMonitorCount(workState);
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
