package com.isuo.yw2application.view.main.data.staff_time;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 16:04
 * E-mail：yangzongbin@si-top.com
 */
final class StaffTimePresenter implements StaffTimeContract.Presenter {
    private CustomerRepository mRepository;
    private StaffTimeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    StaffTimePresenter(CustomerRepository repository, StaffTimeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }

    @Override
    public void getChartData(String time) {

    }

    @Override
    public void getDeptId(String types) {

    }

    @Override
    public void getChartData(long deptId, String time) {

    }
}