package com.isuo.yw2application.view.main.data.statistics.person;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
import com.isuo.yw2application.mode.customer.CustomerRepository;

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
    public void getCStatisticsPersonData(String startTime, String endTime,int userId) {
        subscription.add(mRepository.getPerson(startTime, endTime,userId, new IObjectCallBack<PartPersonStatistics>() {
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
