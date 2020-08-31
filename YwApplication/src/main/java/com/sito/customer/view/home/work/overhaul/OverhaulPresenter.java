package com.sito.customer.view.home.work.overhaul;

import android.support.annotation.NonNull;


import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.work.WorkRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 检修列表
 * Created by zhangan on 2017-06-22.
 */

class OverhaulPresenter implements OverhaulContract.Presenter {

    private final WorkRepository mRepository;
    private final OverhaulContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    OverhaulPresenter(WorkRepository mRepository, OverhaulContract.View mView) {
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
    public void getData(@NonNull String time) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getOverhaulData(time, null, new IListCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull List<OverhaulBean> list) {
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
