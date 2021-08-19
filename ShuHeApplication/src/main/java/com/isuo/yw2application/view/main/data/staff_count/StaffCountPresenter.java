package com.isuo.yw2application.view.main.data.staff_count;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.count.ComeCount;
import com.isuo.yw2application.mode.bean.count.MonthCount;
import com.isuo.yw2application.mode.bean.count.WeekCount;
import com.isuo.yw2application.mode.bean.count.WeekList;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 16:07
 * E-mail：yangzongbin@si-top.com
 */
final class StaffCountPresenter implements StaffCountContract.Presenter {
    private CustomerRepository mRepository;
    private StaffCountContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    StaffCountPresenter(CustomerRepository repository, StaffCountContract.View view) {
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
    public void getDeptId(String types) {
        mSubscruptions.add(mRepository.getDeptTypeId(types, new IListCallBack<DeptType>() {
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
    public void getComeCount(String time, String deptId) {
        mSubscruptions.add(mRepository.getTodayCount(time, deptId, new IListCallBack<ComeCount>() {
            @Override
            public void onSuccess(@NonNull List<ComeCount> list) {
                mView.showComeCount(list);
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
    public void getComeCount(String startTime, String endTime, String deptId) {
        mSubscruptions.add(mRepository.getSituation(startTime, endTime, deptId, new IListCallBack<ComeCount>() {
            @Override
            public void onSuccess(@NonNull List<ComeCount> list) {
                mView.showComeCount(list);
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
    public void getWeekCount(String time, String deptId) {
        mSubscruptions.add(mRepository.getWeekCount(time, deptId, new IListCallBack<WeekCount>() {
            @Override
            public void onSuccess(@NonNull List<WeekCount> list) {
                mView.showWeekCount(list);
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
    public void getMonthCount(String time, String deptId) {
        mSubscruptions.add(mRepository.getMonthCount(time, deptId, new IListCallBack<MonthCount>() {
            @Override
            public void onSuccess(@NonNull List<MonthCount> list) {
                mView.showMonthCount(list);
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
    public void getWeekList(String time, String deptId) {
        mSubscruptions.add(mRepository.getWeekList(time, deptId, new IListCallBack<WeekList>() {
            @Override
            public void onSuccess(@NonNull List<WeekList> list) {
                mView.showWeekList(list);
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
}