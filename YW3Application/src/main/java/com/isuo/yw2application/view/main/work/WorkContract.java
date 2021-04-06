package com.isuo.yw2application.view.main.work;

import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 工作首页
 * Created by Administrator on 2017/6/28.
 */
interface WorkContract {

    interface Presenter extends BasePresenter {

        void getWorkItem();

        void getNews();

        void getFireData();

    }

    interface View extends BaseView<Presenter> {

        void showWorkItemList(List<WorkItem> workItems);

        void showWorkNews(MessageListBean bean);

        void showAlarmNews(MessageListBean bean);

        void showMyNews(MessageListBean bean);

        void showEnterpriseNews(MessageListBean bean);

        void requestFinish();

        void showFireData();

    }

}