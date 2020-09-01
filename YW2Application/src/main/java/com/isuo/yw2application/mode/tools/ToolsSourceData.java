package com.isuo.yw2application.mode.tools;

import android.support.annotation.NonNull;


import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.isuo.yw2application.mode.tools.bean.ToolsLog;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import rx.Subscription;

/**
 * 工具
 * Created by zhangan on 2018/4/2.
 */

public interface ToolsSourceData {

    @NonNull
    Subscription getToolsList(Map<String, String> map, IListCallBack<Tools> iListCallBack);

    interface IUploadToolsCallBack {

        void onSuccess(String url);

        void onError();
    }

    @NonNull
    Subscription uploadImageFile(File file, @NonNull IUploadToolsCallBack callBack);

    @NonNull
    Subscription addTools(Tools tools, @NonNull IUploadToolsCallBack callBack);

    @NonNull
    Subscription borrowTools(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getToolsLog(Long toolsId, @NonNull IObjectCallBack<ToolsLog> callBack);

    @NonNull
    Subscription returnTools(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getToolsDetail(Long toolsId, IObjectCallBack<Tools> callBack);

    @NonNull
    Subscription getCheckList(Long map, IListCallBack<CheckListBean> iListCallBack);

    @NonNull
    Subscription askReturn(long logId, IObjectCallBack<String> callBack);

    @NonNull
    Subscription getToolsState(long toolsId, IObjectCallBack<String> callBack);

}
