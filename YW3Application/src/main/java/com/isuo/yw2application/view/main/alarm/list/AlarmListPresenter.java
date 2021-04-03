package com.isuo.yw2application.view.main.alarm.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.mode.fault.FaultRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 故障列表
 * Created by zhangan on 2017-06-30.
 */

class AlarmListPresenter implements AlarmListContact.Presenter {

    private FaultRepository mRepository;
    private AlarmListContact.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    AlarmListPresenter(FaultRepository repository, AlarmListContact.View view) {
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
    public void getEquipmentType() {
        mSubscription.add(mRepository.getEquipmentType(new IListCallBack<EquipType>() {
            @Override
            public void onSuccess(@NonNull List<EquipType> list) {
                mView.showEquipmentType(list);
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
    public void getFaultList(@Nullable String equipmentType, @Nullable String alarmType
            , @Nullable String alarmState
            , @Nullable String startTimeStr
            , @Nullable String endTimeStr
            , @Nullable String lastId) {
        mSubscription.add(mRepository.getFaultList(equipmentType, alarmType
                , alarmState
                , startTimeStr
                , endTimeStr, lastId, new IListCallBack<FaultList>() {

                    @Override
                    public void onSuccess(@NonNull List<FaultList> list) {
                        mView.showFaultListMore(list);
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
    public void getFaultList(@Nullable String equipmentType, @Nullable String alarmType, @Nullable String alarmState
            , @Nullable String startTimeStr, @Nullable String endTimeStr) {
        mView.showLoading();
        mSubscription.add(mRepository.getFaultList(equipmentType, alarmType, alarmState, startTimeStr, endTimeStr, null, new IListCallBack<FaultList>() {
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
}
