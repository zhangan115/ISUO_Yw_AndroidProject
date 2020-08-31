package com.sito.evpro.inspection.view.repair.inspection.inspectdetial.secure;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/23 16:52
 * E-mail：yangzongbin@si-top.com
 */
final class SecurePresenter implements SecureContract.Presenter {
    private InspectionRepository mRepository;
    private SecureContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    SecurePresenter(InspectionRepository repository, SecureContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getSecureInfo(long securityId) {
        mView.showLoading();
        mSubscruptions.add(mRepository.getSecureInfo(securityId, new IObjectCallBack<SecureBean>() {
            @Override
            public void onSuccess(@NonNull SecureBean secureBean) {
                mView.showData(secureBean);
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}