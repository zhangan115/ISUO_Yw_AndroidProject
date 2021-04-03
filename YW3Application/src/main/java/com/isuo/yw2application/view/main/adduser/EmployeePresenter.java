package com.isuo.yw2application.view.main.adduser;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.fault.FaultRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-26.
 */

class EmployeePresenter implements EmployeeContract.Presenter {


    @NonNull
    private CompositeSubscription mSubscriptions;
    private final FaultRepository mRepository;
    private final EmployeeContract.View mView;

    EmployeePresenter(FaultRepository mRepository, EmployeeContract.View mView) {
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
    public void getEmployee(boolean isRepair) {
        mView.showLoading();
        Integer type = null;
        if (isRepair) {
            type = 2;
        }
        mSubscriptions.add(mRepository.getEmployeeList(type, new IListCallBack<DepartmentBean>() {
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
