package com.sito.customer.view.home.discover.statistics.part;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 统计
 * Created by zhangan on 2018/4/18.
 */

class StatisticsPartPresenter implements StatisticsPartContract.Presenter {

    private final CustomerRepository mRepository;
    private final StatisticsPartContract.View mView;
    private CompositeSubscription subscription;

    StatisticsPartPresenter(CustomerRepository mRepository, StatisticsPartContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void getCStatisticsPartData(String startTime, String endTime) {
        mView.showLoading();
        subscription.add(mRepository.getPartData(startTime, endTime, new IObjectCallBack<PartPersonStatistics>() {
            @Override
            public void onSuccess(@NonNull PartPersonStatistics statistics) {
                mView.showData(statistics);
            }

            @Override
            public void onError(String message) {

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
        subscription.clear();
    }
}
