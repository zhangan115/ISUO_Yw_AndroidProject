package com.isuo.yw2application.view.main.work.inject.filter;


import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.inject.InjectOilDataSource;
import com.isuo.yw2application.mode.inject.bean.InjectRoomBean;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 注油筛选
 * Created by zhangan on 2018/4/10.
 */

class InjectFilterPresenter implements InjectFilterContract.Presenter {

    private final InjectOilDataSource mRepository;
    private final InjectFilterContract.View mView;
    private CompositeSubscription mSubscriptions;

    InjectFilterPresenter(InjectOilDataSource mRepository, InjectFilterContract.View mView) {
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
    public void requestRoomList() {
        mSubscriptions.add(mRepository.getInjectRoomList(new IListCallBack<InjectRoomBean>() {

            @Override
            public void onSuccess(@NonNull List<InjectRoomBean> list) {
                mView.showRoomList(list);
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
}
