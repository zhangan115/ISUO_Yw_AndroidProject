package com.isuo.yw2application.view.main.work.tool.return_tools;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.tools.ToolsSourceData;
import com.isuo.yw2application.mode.tools.bean.ToolsLog;

import org.json.JSONObject;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

class ReturnToolsPresenter implements ReturnToolsContract.Presenter {

    private final ToolsSourceData sourceData;
    private final ReturnToolsContract.View mView;
    private CompositeSubscription subscription;

    ReturnToolsPresenter(ToolsSourceData sourceData, ReturnToolsContract.View mView) {
        this.sourceData = sourceData;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getToolsLog(Long toolsId) {
        subscription.add(sourceData.getToolsLog(toolsId, new IObjectCallBack<ToolsLog>() {
            @Override
            public void onSuccess(@NonNull ToolsLog toolsLog) {
                mView.showToolsLog(toolsLog);
            }

            @Override
            public void onError(String message) {
                mView.toolsLogError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void returnTools(JSONObject jsonObject) {
        subscription.add(sourceData.returnTools(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.returnSuccess();
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
    public void uploadImage(File image) {
        subscription.add(sourceData.uploadImageFile(image, new ToolsSourceData.IUploadToolsCallBack() {
            @Override
            public void onSuccess(String url) {
                mView.uploadImageSuccess(url);
            }

            @Override
            public void onError() {
                mView.uploadImageFail();
            }
        }));
    }
}
