package com.sito.customer.view.home.discover.faultreport;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.bean.discover.FaultReport;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 14:50
 * E-mail：yangzongbin@si-top.com
 */
final class FaultReportPresenter implements FaultReportContract.Presenter {
    private CustomerRepository mRepository;
    private FaultReportContract.View mView;
    @NonNull
    private CompositeSubscription compositeSubscription;

    @Inject
    FaultReportPresenter(CustomerRepository repository, FaultReportContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getChartData(String time) {

    }

    @Override
    public void getDeptId(String types) {
        compositeSubscription.add(mRepository.getDeptTypeId(types, new IListCallBack<DeptType>() {
            @Override
            public void onSuccess(@NonNull List<DeptType> list) {
                mView.showDeptId(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getChartData(long deptId, String time) {
        mView.showLoading();
        compositeSubscription.add(mRepository.getFaultReport(deptId, time, new IListCallBack<FaultReport>() {
            @Override
            public void onSuccess(@NonNull List<FaultReport> list) {
                mView.showChartData(list);
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeSubscription.clear();
    }
}