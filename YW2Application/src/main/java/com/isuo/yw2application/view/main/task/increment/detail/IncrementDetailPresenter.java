package com.isuo.yw2application.view.main.task.increment.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.work.WorkRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/17.
 */

class IncrementDetailPresenter implements IncrementDetailContract.Presenter {
    @NonNull
    private CompositeSubscription mSubscriptions;
    private final WorkRepository mRepository;
    private final IncrementDetailContract.View mView;

    IncrementDetailPresenter(WorkRepository mRepository, IncrementDetailContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void getIncrementData(long workId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getIncrement(workId, new IObjectCallBack<IncrementBean>() {
            @Override
            public void onSuccess(@NonNull IncrementBean dataBean) {
                mView.showIncrement(dataBean);
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
