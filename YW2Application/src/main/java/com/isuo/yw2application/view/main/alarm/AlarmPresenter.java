package com.isuo.yw2application.view.main.alarm;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCount;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.fault.FaultRepository;
import com.isuo.yw2application.mode.work.WorkDataSource;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/28.
 */
public class AlarmPresenter implements AlarmContract.Presenter {
    private CustomerRepository mRepository;
    private WorkDataSource mWorkDataRepository;
    private FaultRepository mFaultRepository;
    private AlarmContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public AlarmPresenter(CustomerRepository repository, FaultRepository faultRepository, WorkDataSource workDataSource, AlarmContract.View view) {
        this.mRepository = repository;
        this.mFaultRepository = faultRepository;
        this.mWorkDataRepository = workDataSource;
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
    public void getAlarmCount() {
        mSubscription.add(mFaultRepository.getFaultCount(new IObjectCallBack<FaultCount>() {
            @Override
            public void onSuccess(@NonNull FaultCount s) {
                mView.showFaultCount(s);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getAlarmList() {
        mSubscription.add(mFaultRepository.getFaultList(null, null
                , "1"
                , null
                , null, null, new IListCallBack<FaultList>() {

                    @Override
                    public void onSuccess(@NonNull List<FaultList> list) {
                        mView.showFaultList(list);
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
    public void getWorkCount() {
        mWorkDataRepository.getWorkState(new IObjectCallBack<WorkState>() {
            @Override
            public void onSuccess(@NonNull WorkState workState) {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
