package com.isuo.yw2application.view.main.work.all;

import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.work.WorkDataSource;

import java.util.List;

public class WorkItemAllPresenter implements WorkItemAllContract.Presenter {

    final private WorkDataSource mRepository;
    final private WorkItemAllContract.View mView;

    WorkItemAllPresenter(WorkDataSource mRepository, WorkItemAllContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void getWorkItem() {
        mRepository.getAllWorkItems(new WorkDataSource.WorkItemAllCallBack() {


            @Override
            public void showWorkItem(List<WorkItem> workItems) {
                mView.showMyWorkItemList(workItems);
            }

            @Override
            public void showAllWorkItem(List<WorkItem> workItems) {
                mView.showAllWorkItemList(workItems);
            }

            @Override
            public void showPayWorkItem(List<WorkItem> workItems) {
                mView.showPayWorkItemList(workItems);
            }
        });
    }

    @Override
    public void saveWorkItem(List<WorkItem> workItems) {
        mRepository.saveWorkItems(workItems);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
