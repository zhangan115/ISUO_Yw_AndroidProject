package com.sito.customer.view.splash;

import android.support.annotation.NonNull;

import com.sito.customer.mode.customer.CustomerDataSource;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-24.
 */

class SplashPresenter implements SplashContract.Presenter {

    private CustomerRepository mRepository;
    private SplashContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    SplashPresenter(CustomerRepository repository, SplashContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mSubscriptions.add(mRepository.autoLogin(new CustomerDataSource.SplashCallBack() {
            @Override
            public void showWelcome() {
                mView.showWelcome();
            }

            @Override
            public void onNeedLogin() {
                mView.needLogin();
            }

            @Override
            public void onAutoFinish() {
                mView.openHome();
            }
        }));
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
