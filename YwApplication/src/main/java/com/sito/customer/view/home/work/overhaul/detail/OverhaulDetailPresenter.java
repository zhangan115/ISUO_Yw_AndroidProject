package com.sito.customer.view.home.work.overhaul.detail;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.work.WorkRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-17.
 */

class OverhaulDetailPresenter implements OverhaulDetailContract.Presenter {
    private final WorkRepository mRepository;
    private final OverhaulDetailContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    OverhaulDetailPresenter(WorkRepository mRepository, OverhaulDetailContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void getRepairDetailData(@NonNull String repairId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getRepairDetail(repairId, new IObjectCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull OverhaulBean bean) {
                mView.showData(bean);
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