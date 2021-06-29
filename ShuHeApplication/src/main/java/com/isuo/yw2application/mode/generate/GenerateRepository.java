package com.isuo.yw2application.mode.generate;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.bean.db.Image;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

/**
 * 发布专项工作，检修工作
 * Created by zhangan on 2017/9/29.
 */
@Singleton
public class GenerateRepository implements GenerateSourceData {

    @Inject
    public GenerateRepository() {
    }


    @Override
    public void deleteFile(String files) {
        if (TextUtils.isEmpty(files)) {
            return;
        }
        File file = new File(files);
        if (file.exists()) {
            file.delete();
        }
    }

    @NonNull
    @Override
    public Subscription uploadImageFile(@NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<Image> callBack) {
        final Image image = new Image();
        image.setImageLocal(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(GenerateApi.class)
                .postFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                if (strings == null || strings.size() == 0) {
                    callBack.onError("上传失败");
                } else {
                    image.setBackUrl(strings.get(0));
                    callBack.onSuccess(image);
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
    public Subscription uploadImageFile(@NonNull String businessType,final @NonNull Image image
            , final @NonNull UploadImageCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(image.getImageLocal());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                if (strings == null || strings.size() == 0) {
                    callBack.onError(image);
                } else {
                    image.setUpload(true);
                    image.setBackUrl(strings.get(0));
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError(image);
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadVoiceFile(@NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(GenerateApi.class)
                .postFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                String url = strings.get(0);
                callBack.onSuccess(url);
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
    public Subscription uploadRepairInfo(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(GenerateApi.class)
                .addRepair(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String uploadResult) {
                callBack.onFinish();
                callBack.onSuccess(uploadResult);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("提交失败");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadIncrementInfo(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(GenerateApi.class)
                .addIncrement(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String uploadResult) {
                callBack.onFinish();
                callBack.onSuccess(uploadResult);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("提交失败");
            }
        }.execute1();
    }

}
