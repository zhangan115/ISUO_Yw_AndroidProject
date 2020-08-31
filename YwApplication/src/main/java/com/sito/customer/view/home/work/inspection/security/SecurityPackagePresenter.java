package com.sito.customer.view.home.work.inspection.security;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.inspection.SecureBean;
import com.sito.customer.mode.inspection.InspectionSourceData;

import rx.subscriptions.CompositeSubscription;

/**
 * 获取安全包
 * Created by zhangan on 2018/3/20.
 */
class SecurityPackagePresenter implements SecurityPackageContract.Presenter {

    private final InspectionSourceData mSourceData;
    private final SecurityPackageContract.View mView;
    private CompositeSubscription mSubscription;

    SecurityPackagePresenter(InspectionSourceData sourceData, SecurityPackageContract.View mView) {
        this.mSourceData = sourceData;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getSecureInfo(long securityId) {
        mView.showLoading();
        mSubscription.add(mSourceData.getSecureInfo(securityId, new IObjectCallBack<SecureBean>() {
            @Override
            public void onSuccess(@NonNull SecureBean secureBean) {
                mView.showData(secureBean);
            }

            @Override
            public void onError(String message) {
                mView.showMessage(message);
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
        mSubscription.clear();
    }
}
