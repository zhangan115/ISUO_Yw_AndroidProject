package com.isuo.yw2application.mode.sos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.sos.EmergencyCall;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

/**
 * 巡检
 * Created by zhangan on 2018/3/20.
 */

public class SOSRepository implements SOSSourceData {

    private static SOSRepository repository;

    private SOSRepository() {

    }

    public static SOSRepository getRepository() {
        if (repository == null) {
            repository = new SOSRepository();
        }
        return repository;
    }


    @NonNull
    @Override
    public Subscription getEmergencyCalls(final IListCallBack<EmergencyCall> callBack) {
        Observable<Bean<List<EmergencyCall>>> observable = Api.createRetrofit().create(SOSApi.class).getEmergencyCalls();
        return new ApiCallBack<List<EmergencyCall>>(observable) {
            @Override
            public void onSuccess(List<EmergencyCall> emergencyCalls) {
                callBack.onFinish();
                if (emergencyCalls != null && emergencyCalls.size() > 0) {
                    callBack.onSuccess(emergencyCalls);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadSoSData(@NonNull JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable1 = Api.createRetrofit().create(SOSApi.class).uploadEmergencyData(jsonObject.toString());
        return new ApiCallBack<String>(observable1) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadFile(@NonNull String filePath, @NonNull String businessType, @NonNull String fileType
            , @NonNull final UploadFileCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", fileType);
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    callBack.uploadSuccess(strings.get(0));
                } else {
                    callBack.uploadFail();
                }
            }

            @Override
            public void onFail() {
                callBack.uploadFail();
            }
        }.execute1();
    }
}