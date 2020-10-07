package com.isuo.yw2application.view.main.alarm.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.fault.FaultDetail;
import com.isuo.yw2application.mode.bean.fault.JobPackageBean;
import com.isuo.yw2application.mode.fault.FaultRepository;

import org.json.JSONObject;

import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 故障流转
 * Created by zhangan on 2017-07-18.
 */

class AlarmDetailPresenter implements AlarmDetailContract.Presenter {

    @NonNull
    private CompositeSubscription mSubscriptions;
    private final FaultRepository mRepository;
    private final AlarmDetailContract.View mView;

    AlarmDetailPresenter(FaultRepository mRepository, AlarmDetailContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void getFaultDetailData(String faultId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getFaultDetail(faultId, new IObjectCallBack<FaultDetail>() {
            @Override
            public void onSuccess(@NonNull FaultDetail faultDetail) {
                mView.showData(faultDetail);
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
    public void closeFault(Map<String, String> map) {
        mView.uploadProgress();
        mSubscriptions.add(mRepository.closeFault(map, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadSuccess();
            }

            @Override
            public void onError(String message) {
                mView.uploadFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void overhaulFault(JSONObject jsonObject) {
        mView.uploadProgress();
        mSubscriptions.add(mRepository.overhaulFault(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadSuccess();
            }

            @Override
            public void onError(String message) {
                mView.uploadFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }


    @Override
    public void uploadFaultInfo(Map<String, String> map) {
        mView.uploadProgress();
        mSubscriptions.add(mRepository.flowFault(map, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadSuccess();
            }

            @Override
            public void onError(String message) {
                mView.uploadFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getJobPackage() {
        mSubscriptions.add(mRepository.getJobPackage(new IObjectCallBack<JobPackageBean>() {
            @Override
            public void onSuccess(@NonNull JobPackageBean jobPackageBean) {
                mView.showJobPackage(jobPackageBean);
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
    public void careEquipment(JSONObject jsonObject) {
        mSubscriptions.add(mRepository.careEquipment(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.careSuccess();
            }

            @Override
            public void onError(String message) {
                mView.careFail();
            }

            @Override
            public void onFinish() {

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
