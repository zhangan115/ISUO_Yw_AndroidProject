package com.sito.customer.view.home.work.tool.borrow;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.tools.ToolsSourceData;
import com.sito.customer.mode.tools.bean.CheckListBean;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

class BorrowPresenter implements BorrowContract.Presenter {

    private final ToolsSourceData sourceData;
    private final BorrowContract.View mView;
    private CompositeSubscription subscription;

    BorrowPresenter(ToolsSourceData sourceData, BorrowContract.View mView) {
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
    public void borrowTools(JSONObject jsonObject) {
        subscription.add(sourceData.borrowTools(jsonObject, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.borrowSuccess();
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
    public void getCheckList(Long toolsId) {
        subscription.add(sourceData.getCheckList(toolsId, new IListCallBack<CheckListBean>() {
            @Override
            public void onSuccess(@NonNull List<CheckListBean> list) {
                mView.showCheckList(list);
            }

            @Override
            public void onError(String message) {
                mView.noCheckList();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getToolsState(Long toolsId) {
        subscription.add(sourceData.getToolsState(toolsId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.toolsCanBorrow();
            }

            @Override
            public void onError(String message) {
                mView.toolsCantBorrow();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
