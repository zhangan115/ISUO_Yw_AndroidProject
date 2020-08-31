package com.sito.customer.mode.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.sito.customer.api.Api;
import com.sito.customer.api.ApiCallBack;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.tools.bean.CheckListBean;
import com.sito.customer.mode.tools.bean.Tools;
import com.sito.customer.mode.tools.bean.ToolsLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

/**
 * tools
 * Created by zhangan on 2018/4/2.
 */

public class ToolsRepository implements ToolsSourceData {

    private static ToolsRepository repository;

    private ToolsRepository(Context context) {

    }

    public static ToolsRepository getRepository(Context context) {
        if (repository == null) {
            repository = new ToolsRepository(context);
        }
        return repository;
    }

    @NonNull
    @Override
    public Subscription getToolsList(Map<String, String> map, final IListCallBack<Tools> iListCallBack) {
        return new ApiCallBack<List<Tools>>(Api.createRetrofit().create(ToolsApi.class).getToolList(map)) {

            @Override
            public void onSuccess(@Nullable List<Tools> tools) {
                iListCallBack.onFinish();
                if (tools != null && tools.size() != 0) {
                    iListCallBack.onSuccess(tools);
                } else {
                    iListCallBack.onError("");
                }
            }

            @Override
            public void onFail() {
                iListCallBack.onFinish();
                iListCallBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadImageFile(File file, final @NonNull IUploadToolsCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "tools")
                .addFormDataPart("fileType", "image");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings == null || strings.size() == 0) {
                    callBack.onError();
                } else {
                    callBack.onSuccess(strings.get(0));
                }
            }

            @Override
            public void onFail() {
                callBack.onError();
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription addTools(Tools tools, @NonNull final IUploadToolsCallBack callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class)
                .addTools(new Gson().toJson(tools))) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onSuccess(s);
            }

            @Override
            public void onFail() {
                callBack.onError();
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription borrowTools(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class)
                .getToolsToolOut(jsonObject.toString())) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getToolsLog(Long toolsId, @NonNull final IObjectCallBack<ToolsLog> callBack) {
        return new ApiCallBack<ToolsLog>(Api.createRetrofit().create(ToolsApi.class)
                .getToolsLog(toolsId)) {
            @Override
            public void onSuccess(@Nullable ToolsLog s) {
                callBack.onSuccess(s);
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription returnTools(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class)
                .userReturnTool(jsonObject.toString())) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getToolsDetail(Long toolsId, final IObjectCallBack<Tools> callBack) {
        return new ApiCallBack<Tools>(Api.createRetrofit().create(ToolsApi.class)
                .getToolsTools(toolsId)) {
            @Override
            public void onSuccess(@Nullable Tools s) {
                callBack.onFinish();
                if (s != null) {
                    callBack.onSuccess(s);
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
    public Subscription getCheckList(Long toolsId, final IListCallBack<CheckListBean> iListCallBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toolId", toolsId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ApiCallBack<List<CheckListBean>>(Api.createRetrofit().create(ToolsApi.class)
                .getCheckList(jsonObject.toString())) {

            @Override
            public void onSuccess(@Nullable List<CheckListBean> checkList) {
                iListCallBack.onFinish();
                if (checkList != null && checkList.size() != 0) {
                    iListCallBack.onSuccess(checkList);
                } else {
                    iListCallBack.onError("");
                }
            }

            @Override
            public void onFail() {
                iListCallBack.onFinish();
                iListCallBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription askReturn(long logId, final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class).askReturnTools(logId)) {
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
    public Subscription getToolsState(long toolsId, final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class).getToolsState(toolsId)) {
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
}
