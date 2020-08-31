package com.sito.customer.view.home;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.mode.customer.CustomerDataSource;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 首页P
 * Created by zhangan on 2017-07-25.
 */

class HomePresenter implements HomeContract.Presenter {

    private CustomerRepository mRepository;
    private HomeContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    HomePresenter(CustomerRepository repository, HomeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getNewVersion() {
        mSubscription.add(mRepository.getNewVersion(new CustomerDataSource.NewVersionCallBack() {
            @Override
            public void newVersion(NewVersion result) {
                mView.showNewVersion(result);
            }

            @Override
            public void noNewVersion() {

            }
        }));
    }

    @Override
    public void postCidInfo(@NonNull String userCid) {
        mSubscription.add(mRepository.postCidInfo(userCid, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {

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
    public void getUnReadCount() {
        mView.showUnReadCount(mRepository.loadUnReadMessageCount());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
