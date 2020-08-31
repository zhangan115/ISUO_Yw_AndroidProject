package com.sito.evpro.inspection.view.contact;

import android.support.annotation.NonNull;


import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.evpro.inspection.mode.employee.EmployeeRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017/9/5.
 */

public class ContactPresenter implements ContactContract.Presenter {

    @NonNull
    private CompositeSubscription mSubscriptions;
    private final EmployeeRepository mRepository;
    private final ContactContract.View mView;

    ContactPresenter(EmployeeRepository mRepository, ContactContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void getEmployee() {
        mView.showLoading();
        mSubscriptions.add(mRepository.getEmployeeList(new IListCallBack<DepartmentBean>() {
            @Override
            public void onSuccess(@NonNull List<DepartmentBean> list) {
                mView.showData(list);
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
        mSubscriptions.clear();
    }
}
