package com.isuo.yw2application.view.main.data.fault_line;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultYearCountBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/28.
 */
public class FaultLinePresenter implements FaultLineContract.Presenter {
    private CustomerRepository mRepository;
    private FaultLineContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    FaultLinePresenter(CustomerRepository repository, FaultLineContract.View view) {
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
