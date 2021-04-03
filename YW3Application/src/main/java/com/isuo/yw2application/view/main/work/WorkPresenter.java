package com.isuo.yw2application.view.main.work;

import android.support.annotation.NonNull;


import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.work.WorkDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 工作
 * Created by Administrator on 2017/6/28.
 */
class WorkPresenter implements WorkContract.Presenter {
    private WorkDataSource mRepository;
    private CustomerRepository mNewsRepository;
    private WorkContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    WorkPresenter(WorkDataSource repository,CustomerRepository newsRepository, WorkContract.View view) {
        this.mRepository = repository;
        this.mNewsRepository = newsRepository;
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

        });
    }

    @Override
    public void getNews() {
        requestWorkNews();
        requestAlarmNews();
        requestMyNews();
        requestEnterpriseNews();
    }

    private void requestWorkNews() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("count", String.valueOf(1));
        map1.put("messageType", String.valueOf(1));
        subscription.add(mNewsRepository.getMessageList(map1, new IListCallBack<MessageListBean>() {
            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showWorkNews(list.get(0));
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.requestFinish();
            }
        }));
    }

    private void requestAlarmNews() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("count", String.valueOf(1));
        map1.put("messageType", String.valueOf(2));
        subscription.add(mNewsRepository.getMessageList(map1, new IListCallBack<MessageListBean>() {
            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showAlarmNews(list.get(0));
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.requestFinish();
            }
        }));
    }

    private void requestEnterpriseNews() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("count", String.valueOf(1));
        map1.put("messageType", String.valueOf(3));
        subscription.add(mNewsRepository.getMessageList(map1, new IListCallBack<MessageListBean>() {
            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showEnterpriseNews(list.get(0));
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.requestFinish();
            }
        }));
    }

    private void requestMyNews() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("count", String.valueOf(1));
        map1.put("messageType", String.valueOf(4));
        subscription.add(mNewsRepository.getMessageList(map1, new IListCallBack<MessageListBean>() {
            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showMyNews(list.get(0));
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.requestFinish();
            }
        }));
    }

}
