package com.sito.customer.view.home.work;

import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.widget.CountWorkLayout;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作首页
 * Created by Administrator on 2017/6/28.
 */
interface WorkContract {

    interface Presenter extends BasePresenter {

        void getWorkItem();

        void getWorkCount();

        void saveWorkItem(ArrayList<WorkItem> workItems);
    }

    interface View extends BaseView<Presenter> {

        void showWorkCount(List<CountWorkLayout.CountWorkBean> countWorkBeans);

        void showWorkItemList(List<WorkItem> workItems);

        void showAllWorkItemList(List<WorkItem> items);
    }

}