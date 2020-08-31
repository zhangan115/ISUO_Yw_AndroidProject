package com.sito.customer.view.home.work.tool.return_tools;

import android.support.annotation.NonNull;

import com.iflytek.cloud.thirdparty.V;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.tools.ToolsSourceData;
import com.sito.customer.mode.tools.bean.CheckListBean;
import com.sito.customer.mode.tools.bean.ToolsLog;

import org.json.JSONObject;

import java.util.List;

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
}
