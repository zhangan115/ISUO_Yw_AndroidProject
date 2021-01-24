package com.isuo.yw2application.view.main.work.tool.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.tools.ToolsSourceData;
import com.isuo.yw2application.mode.tools.bean.ToolLogListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void getToolBrowList(Long logId) {
        Map<String,String> map = new HashMap<>();
        map.put("toolId",logId.toString());
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        subscription.add(sourceData.getBrowList(map, new IObjectCallBack<ToolLogListBean>() {
            @Override
            public void onSuccess(@NonNull ToolLogListBean bean) {
                mView.showToolBrowList(bean.getList());
            }

            @Override
            public void onError(String message) {
                mView.noToolBrowLog();
            }

            @Override
            public void onFinish() {
                mView.refreshFinish();
            }
        }));
    }

    @Override
    public void getToolBrowListMore(Long logId,Long lastId) {
        Map<String,String> map = new HashMap<>();
        map.put("toolId",logId.toString());
        map.put("lastId",lastId.toString());
        map.put("count", String.valueOf(ConstantInt.PAGE_SIZE));
        subscription.add(sourceData.getBrowList(map, new IObjectCallBack<ToolLogListBean>() {
            @Override
            public void onSuccess(@NonNull ToolLogListBean bean) {
                mView.showToolBrowListMore(bean.getList());
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
