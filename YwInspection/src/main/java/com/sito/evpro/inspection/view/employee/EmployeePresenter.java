package com.sito.evpro.inspection.view.employee;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.evpro.inspection.mode.employee.EmployeeRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-26.
 */

public class EmployeePresenter implements EmployeeContract.Presenter {


    @NonNull
    private CompositeSubscription mSubscriptions;
    private final EmployeeRepository mRepository;
    private final EmployeeContract.View mView;

    public EmployeePresenter(EmployeeRepository mRepository, EmployeeContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
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
}
