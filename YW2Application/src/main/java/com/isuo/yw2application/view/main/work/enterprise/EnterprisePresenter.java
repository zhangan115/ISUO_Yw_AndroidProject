package com.isuo.yw2application.view.main.work.enterprise;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.news.EnterpriseDetailBean;
import com.isuo.yw2application.mode.customer.CustomerDataSource;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 通知详情
 * Created by zhangan on 2018/4/23.
 */

class EnterprisePresenter implements EnterpriseContract.Presenter {

    private final CustomerRepository mRepository;
    private final EnterpriseContract.View mView;
    private CompositeSubscription mSubscription;

    EnterprisePresenter(CustomerRepository mRepository, EnterpriseContract.View mView) {
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
    public void getMessageDetail(long messageId) {
        mSubscription.add(mRepository.getMessageDetail(messageId, new IObjectCallBack<EnterpriseDetailBean>() {
            @Override
            public void onSuccess(@NonNull EnterpriseDetailBean s) {
                mView.showEnterpriseDetailBean(s);
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
    public void downLoadFile(String url, String savePath, String fileName) {
        mView.showDownLoading();
        mRepository.downLoadFile(fileName, savePath, url, new CustomerDataSource.DownLoadCallBack() {
            @Override
            public void onSuccess(String fileName, String filePath) {
                mView.hideDownLoading();
                mView.downLoadSuccess();
            }

            @Override
            public void onFile() {
                mView.hideDownLoading();
            }
        });
    }
}
