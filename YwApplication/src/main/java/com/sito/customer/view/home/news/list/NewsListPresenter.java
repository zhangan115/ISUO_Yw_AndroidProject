package com.sito.customer.view.home.news.list;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.news.MessageListBean;
import com.sito.customer.mode.customer.CustomerRepository;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 显示消息列表
 * Created by zhangan on 2017/10/16.
 */

class NewsListPresenter implements NewsListContract.Presenter {

    private CustomerRepository mRepository;
    private NewsListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    NewsListPresenter(CustomerRepository mRepository, NewsListContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void borrowSure(long toolsId, int state) {
        mSubscription.add(mRepository.borrowedSure(toolsId,state, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.borrowSureSuccess();
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
    public void getMessageList(Map<String, String> map) {
        mView.showLoading();
        mSubscription.add(mRepository.getMessageList(map, new IListCallBack<MessageListBean>() {

            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showMessageList(list);
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
    public void getMessageListMore(Map<String, String> map) {
        mSubscription.add(mRepository.getMessageList(map, new IListCallBack<MessageListBean>() {
            @Override
            public void onSuccess(@NonNull List<MessageListBean> list) {
                mView.showMessageListMore(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.loadMoreFinish();
            }
        }));
    }

}
