package com.isuo.yw2application.view.main.work.all;

import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

public interface WorkItemAllContract {
    interface Presenter extends BasePresenter {

        void getWorkItem();

    }

    interface View extends BaseView<WorkItemAllContract.Presenter> {

        void showMyWorkItemList(List<WorkItem> workItems);

        void showAllWorkItemList(List<WorkItem> workItems);

        void showPayWorkItemList(List<WorkItem> workItems);
    }
}
