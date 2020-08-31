package com.sito.customer.mode.generate;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.bean.db.Image;

import org.json.JSONObject;

import rx.Subscription;

/**
 * Created by zhangan on 2017/9/29.
 */

public interface GenerateSourceData {

    void deleteFile(String files);

    @NonNull
    Subscription uploadImageFile(@NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<Image> callBack);


    @NonNull
    Subscription uploadVoiceFile(@NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<String> callBack);

    @NonNull
    Subscription uploadRepairInfo(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription uploadIncrementInfo(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription uploadImageFile( @NonNull String businessType, @NonNull Image image, @NonNull UploadImageCallBack callBack);
}
