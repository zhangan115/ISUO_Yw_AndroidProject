package com.isuo.yw2application.view.main.work.await;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.work.AwaitWorkBean;
import com.isuo.yw2application.mode.work.WorkRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 代办工作列表（P）
 * Created by zhangan on 2017-06-29.
 */

public class AwaitPresenter implements AwaitContract.Presenter {

    @NonNull
    private CompositeSubscription mSubscriptions;
    private final WorkRepository mRepository;
    private final AwaitContract.View mView;

    public AwaitPresenter(WorkRepository mRepository, AwaitContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getData(String date) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getAwaitWorkData(date, null, new IListCallBack<AwaitWorkBean>() {
            @Override
            public void onSuccess(@NonNull List<AwaitWorkBean> list) {
                mView.showData(list);
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
    public void getDataMore(String date, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getAwaitWorkData(date, lastId, new IListCallBack<AwaitWorkBean>() {
            @Override
            public void onSuccess(@NonNull List<AwaitWorkBean> list) {
                mView.showMoreData(list);
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
