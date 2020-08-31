package com.sito.customer.view.home.work.tool.detail;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.tools.ToolsSourceData;

import rx.subscriptions.CompositeSubscription;

/**
 * tools
 * Created by zhangan on 2018/4/8.
 */

class ToolsDetailPresenter implements ToolsDetailContract.Presenter {

    private final ToolsSourceData sourceData;
    private final ToolsDetailContract.View mView;
    private CompositeSubscription subscription;

    ToolsDetailPresenter(ToolsSourceData sourceData, ToolsDetailContract.View mView) {
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
    public void askReturn(Long logId) {
        subscription.add(sourceData.askReturn(logId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.askSuccess();
            }

            @Override
            public void onError(String message) {
                mView.askError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
