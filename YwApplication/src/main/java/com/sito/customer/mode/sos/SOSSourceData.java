package com.sito.customer.mode.sos;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.sos.EmergencyCall;
import com.sito.customer.mode.overhaul.OverhaulSourceData;

import org.json.JSONObject;

import rx.Subscription;

/**
 * sos
 * Created by zhangan on 2018/3/29.
 */

public interface SOSSourceData {

    @NonNull
    Subscription getEmergencyCalls(IListCallBack<EmergencyCall> iListCallBack);

    @NonNull
    Subscription uploadSoSData(@NonNull JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);



    interface UploadFileCallBack {

        void uploadSuccess(String url);

        void uploadFail();
    }


    @NonNull
    Subscription uploadFile(@NonNull String filePath, @NonNull String businessType
            , @NonNull String fileType, @NonNull UploadFileCallBack callBack);

}
