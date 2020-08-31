package com.sito.customer.view.home.discover.statistics.person;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 统计 个人
 * Created by zhangan on 2018/4/18.
 */

public class StatisticsPersonPresenter implements StatisticsPersonContract.Presenter {


    private final CustomerRepository mRepository;
    private final StatisticsPersonContract.View mView;
    private CompositeSubscription subscription;

    StatisticsPersonPresenter(CustomerRepository mRepository, StatisticsPersonContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }


    @Override
    public void getCStatisticsPersonData(String startTime, String endTime) {
        subscription.add(mRepository.getPerson(startTime, endTime, new IObjectCallBack<PartPersonStatistics>() {
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
}
