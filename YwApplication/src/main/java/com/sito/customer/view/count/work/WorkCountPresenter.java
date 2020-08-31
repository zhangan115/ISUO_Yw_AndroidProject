package com.sito.customer.view.count.work;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.count.WorkCount;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.count.CountRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-19.
 */

class WorkCountPresenter implements WorkCountContract.Presenter {

    private CountRepository mRepository;
    private WorkCountContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    WorkCountPresenter(CountRepository repository, WorkCountContract.View view) {
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
    public void getWorkCountData(String deptId, String time) {
        mSubscription.add(mRepository.getWorkCountData(deptId, time, new IListCallBack<WorkCount>() {
            @Override
            public void onSuccess(@NonNull List<WorkCount> list) {
                mView.showWorkCountData(list);
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
