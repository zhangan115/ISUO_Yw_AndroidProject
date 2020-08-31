package com.sito.customer.view.home.work.tool;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.tools.ToolsSourceData;
import com.sito.customer.mode.tools.bean.Tools;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 工具
 * Created by zhangan on 2018/4/2.
 */

class ToolsPresenter implements ToolsContract.Presenter {

    private final ToolsSourceData mRepository;
    private final ToolsContract.View mView;
    private CompositeSubscription mSubscription;

    ToolsPresenter(ToolsSourceData mRepository, ToolsContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
    }


    @Override
    public void subscribe() {
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void searchTools(Map<String, String> map) {
        mView.showLoading();
        mSubscription.add(mRepository.getToolsList(map, new IListCallBack<Tools>() {
            @Override
            public void onSuccess(@NonNull List<Tools> list) {
                mView.showTools(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getToolsList(Map<String, String> map) {
        mView.showLoading();
        mSubscription.add(mRepository.getToolsList(map, new IListCallBack<Tools>() {
            @Override
            public void onSuccess(@NonNull List<Tools> list) {
                mView.showTools(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getToolsListMore(Map<String, String> map) {
        mSubscription.add(mRepository.getToolsList(map, new IListCallBack<Tools>() {
            @Override
            public void onSuccess(@NonNull List<Tools> list) {
                mView.showMoreTools(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }
}
