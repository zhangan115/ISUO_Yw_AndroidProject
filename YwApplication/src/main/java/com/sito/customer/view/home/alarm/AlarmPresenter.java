package com.sito.customer.view.home.alarm;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.fault.AlarmCount;
import com.sito.customer.mode.bean.fault.FaultCountBean;
import com.sito.customer.mode.bean.fault.FaultDayCountBean;
import com.sito.customer.mode.bean.fault.FaultYearCountBean;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/28.
 */
public class AlarmPresenter implements AlarmContract.Presenter {
    private CustomerRepository mRepository;
    private AlarmContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public AlarmPresenter(CustomerRepository repository, AlarmContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void getFaultCount() {
        mSubscription.add(mRepository.getFaultCount(new IObjectCallBack<FaultCountBean>() {
            @Override
            public void onSuccess(@NonNull FaultCountBean faultCountBean) {
                mView.showFaultCount(faultCountBean);
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
    public void getFaultDayCount(@NonNull String time) {
        mSubscription.add(mRepository.getFaultDayCount(time, new IObjectCallBack<FaultDayCountBean>() {
            @Override
            public void onSuccess(@NonNull FaultDayCountBean bean) {
                mView.showFaultDayCount(bean);
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
    public void getFaultYearCount(@NonNull String time) {
        mSubscription.add(mRepository.getFaultYearCount(time, new IObjectCallBack<FaultYearCountBean>() {
            @Override
            public void onSuccess(@NonNull FaultYearCountBean bean) {
                mView.showFaultYearCount(bean);
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
    public void getAlarmCount() {
        mSubscription.add(mRepository.getAlarmCount(new IObjectCallBack<AlarmCount>() {
            @Override
            public void onSuccess(@NonNull AlarmCount alarmCount) {
                mView.showAlarmCount(alarmCount);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
