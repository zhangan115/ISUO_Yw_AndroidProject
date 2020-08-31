package com.sito.evpro.inspection.view.sos;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.EmergencyCall;
import com.sito.evpro.inspection.mode.inspection.InspectionDataSource;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import org.json.JSONObject;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * sos
 * Created by zhangan on 2017/8/23.
 */

class SOSPresenter implements SOSContract.Presenter {
    private InspectionRepository mRepository;
    private SOSContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    SOSPresenter(InspectionRepository mRepository, SOSContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void emergencyCalls() {
        mSubscription.add(mRepository.getEmergencyCalls(new IListCallBack<EmergencyCall>() {
            @Override
            public void onSuccess(@NonNull List<EmergencyCall> list) {
                mView.showEmergencyCalls(list);
            }

            @Override
            public void onError(String message) {
                mView.emergencyCallError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void uploadEmergencyData(JSONObject jsonObject) {
        mView.showUploadProgress();
        mSubscription.add(mRepository.uploadSoSData(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadDataSuccess();
            }

            @Override
            public void onError(String message) {
                mView.uploadDataFail(message);
            }

            @Override
            public void onFinish() {
                mView.hideUploadProgress();
            }
        }));
    }

    @Override
    public void uploadVoiceFile(@NonNull String voiceLocalPath, String businessType, String fileType) {
        mSubscription.add(mRepository.uploadFile(voiceLocalPath, businessType, fileType, new InspectionDataSource.UploadFileCallBack() {
            @Override
            public void uploadSuccess(String url) {
                mView.uploadVoiceSuccess(url);
            }

            @Override
            public void uploadFail() {
                mView.uploadVoiceFail();
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
