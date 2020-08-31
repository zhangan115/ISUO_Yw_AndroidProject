package com.sito.customer.view.contact;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.employee.DepartmentBean;
import com.sito.customer.mode.fault.FaultRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017/9/5.
 */

public class ContactPresenter implements ContactContract.Presenter {

    @NonNull
    private CompositeSubscription mSubscriptions;
    private final FaultRepository mRepository;
    private final ContactContract.View mView;

    ContactPresenter(FaultRepository mRepository, ContactContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void getEmployee() {
        mView.showLoading();
        mSubscriptions.add(mRepository.getEmployeeList(null, new IListCallBack<DepartmentBean>() {
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

    }
}
