package com.sito.customer.view.home.news;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.news.Aggregate;
import com.sito.customer.mode.customer.CustomerDataSource;
import com.sito.customer.mode.customer.CustomerRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 消息
 * Created by Administrator on 2017/6/28.
 */
public class NewsPresenter implements NewsContract.Presenter {
    private CustomerRepository mRepository;
    private NewsContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    public NewsPresenter(CustomerRepository repository, NewsContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }


    @Override
    public void getUnReadCount() {
        mSubscription.add(mRepository.getUnreadCount(new CustomerDataSource.UnReadCountCallBack() {
            @Override
            public void onReadCount(int[] count) {
                mView.showUnReadCount(count);
            }
        }));
    }
}
