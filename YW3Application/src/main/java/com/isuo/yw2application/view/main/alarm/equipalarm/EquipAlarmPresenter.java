package com.isuo.yw2application.view.main.alarm.equipalarm;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 作者：Yangzb on 2017/6/30 11:04
 * 邮箱：yangzongbin@si-top.com
 */
final class EquipAlarmPresenter implements EquipAlarmContract.Presenter {
    private CustomerRepository mRepository;
    private EquipAlarmContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    EquipAlarmPresenter(CustomerRepository repository, EquipAlarmContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        subscription = new CompositeSubscription();
        setUpListeners();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getTodayFault(String startTime, String endTime) {
        mView.showLoading();
        subscription.add(mRepository.getTodayFault(startTime, endTime, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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
    public void getTodayFault(String startTime, String endTime, int alarmType) {
        Map<String, String> map = new HashMap<>();
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        map.put("faultState", String.valueOf(alarmType + 1));
        if (!TextUtils.isEmpty(startTime)){
            map.put("startTime", startTime);
        }
        if (!TextUtils.isEmpty(endTime)){
            map.put("endTime", endTime);
        }
        subscription.add(mRepository.getAlarmList(map, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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
    public void getFaultList() {
        mView.showLoading();
        subscription.add(mRepository.getTodayFault(1, null, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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

    /**
     * 遗留故障
     *
     * @param alarmType 类型 a b c
     */
    @Override
    public void getFaultList(int alarmType) {
        Map<String, String> map = new HashMap<>();
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        map.put("faultState", String.valueOf(1));
        map.put("faultType", String.valueOf(alarmType + 1));
        subscription.add(mRepository.getAlarmList(map, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showFaultList(list);
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
    public void getMoreFaultList(long lastId) {
        subscription.add(mRepository.getTodayFault(1, lastId, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }

    @Override
    public void getMoreFaultList(long lastId, int alarmType) {
        Map<String, String> map = new HashMap<>();
        map.put("lastId", String.valueOf(lastId));
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        map.put("faultState", String.valueOf(1));
        map.put("faultType", String.valueOf(alarmType + 1));
        subscription.add(mRepository.getAlarmList(map, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }

    @Override
    public void getMoreTodayFault(long lastId, String startTime, String endTime) {
        subscription.add(mRepository.getMoreTodayFault(lastId, startTime, endTime, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }

    @Override
    public void getMoreTodayFault(long lastId, String startTime, String endTime, int alarmType) {
        Map<String, String> map = new HashMap<>();
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("faultState", String.valueOf(alarmType + 1));
        map.put("lastId", String.valueOf(lastId));
        subscription.add(mRepository.getAlarmList(map, new IListCallBack<FaultList>() {
            @Override
            public void onSuccess(@NonNull List<FaultList> list) {
                mView.showMoreFaultList(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }
}