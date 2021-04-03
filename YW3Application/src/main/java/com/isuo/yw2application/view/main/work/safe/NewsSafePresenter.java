package com.isuo.yw2application.view.main.work.safe;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 火警，救护详情
 * Created by zhangan on 2018/4/23.
 */

class NewsSafePresenter implements NewsSafeContract.Presenter {

    private final CustomerRepository mRepository;
    private final NewsSafeContract.View mView;
    private CompositeSubscription mSubscription;

    NewsSafePresenter(CustomerRepository mRepository, NewsSafeContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getMessageDetail(long messageId) {
        mSubscription.add(mRepository.getFireSaveMessage(messageId, new IObjectCallBack<MessageListBean>() {

            @Override
            public void onSuccess(@NonNull MessageListBean s) {
                mView.showData(s);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
