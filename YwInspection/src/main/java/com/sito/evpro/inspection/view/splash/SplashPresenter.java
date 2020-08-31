package com.sito.evpro.inspection.view.splash;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-24.
 */

class SplashPresenter implements SplashContract.Presenter {

    private InspectionRepository mRepository;
    private SplashContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    SplashPresenter(InspectionRepository repository, SplashContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mSubscriptions.add(mRepository.autoLogin(new InspectionRepository.SplashCallBack() {

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
