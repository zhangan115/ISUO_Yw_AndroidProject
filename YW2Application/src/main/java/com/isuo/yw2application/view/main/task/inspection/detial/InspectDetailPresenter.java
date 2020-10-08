package com.isuo.yw2application.view.main.task.inspection.detial;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.CheckBean;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/17 19:36
 * E-mail：yangzongbin@si-top.com
 */
final class InspectDetailPresenter implements InspectDetailContract.Presenter {
    private CustomerRepository mRepository;
    private InspectDetailContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    InspectDetailPresenter(CustomerRepository repository, InspectDetailContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        subscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getCheckInfo(long taskId) {
        mView.showLoading();
        subscription.add(mRepository.getCheckInfo(taskId, new IObjectCallBack<CheckBean>() {
            @Override
            public void onSuccess(@NonNull CheckBean bean) {
                mView.showCheckInfo(bean);
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
    public void getFaultList(long taskId) {
        mView.showLoading();
        subscription.add(mRepository.getFaultList(taskId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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
    public void getMoreFaultList(long taskId, int lastId) {
        subscription.add(mRepository.getMoreFaultList(taskId, lastId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}