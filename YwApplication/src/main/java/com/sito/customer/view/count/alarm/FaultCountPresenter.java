package com.sito.customer.view.count.alarm;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.count.FaultCount;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.count.CountRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-19.
 */

class FaultCountPresenter implements FaultCountContract.Presenter {

    private CountRepository mRepository;
    private FaultCountContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    FaultCountPresenter(CountRepository repository, FaultCountContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getDeptList(String deptType) {
        mSubscription.add(mRepository.getDeptTypeList(deptType, new IListCallBack<DeptType>() {
            @Override
            public void onSuccess(@NonNull List<DeptType> list) {
                mView.showDeptList(list);
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
    public void getFaultCountData(String deptId, String time) {
        mSubscription.add(mRepository.getFaultCountData(deptId, time, new IListCallBack<FaultCount>() {
            @Override
            public void onSuccess(@NonNull List<FaultCount> list) {
                mView.showFaultCountData(list);
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
