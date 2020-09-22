package com.isuo.yw2application.view.main.work;

import android.support.annotation.NonNull;


import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.work.WorkDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 工作
 * Created by Administrator on 2017/6/28.
 */
class WorkPresenter implements WorkContract.Presenter {
    private WorkDataSource mRepository;
    private WorkContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    WorkPresenter(WorkDataSource repository, WorkContract.View view) {
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
    public void getWorkItem() {
        mRepository.getWorkItems(new WorkDataSource.WorkItemCallBack() {
            @Override
            public void showWorkItem(List<WorkItem> workItems) {
                mView.showWorkItemList(workItems);
            }

            @Override
            public void showAllWorkItem(List<WorkItem> workItems) {

            }
        });
    }

    @Override
    public void getNews() {

    }

}
