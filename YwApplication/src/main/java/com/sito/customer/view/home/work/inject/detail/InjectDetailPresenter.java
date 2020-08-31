package com.sito.customer.view.home.work.inject.detail;

import android.support.annotation.NonNull;

import com.sito.customer.common.ConstantInt;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.inject.InjectOilDataSource;
import com.sito.customer.mode.inject.bean.InjectEquipmentLog;

import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 注油详情
 * Created by zhangan on 2018/4/12.
 */
class InjectDetailPresenter implements InjectDetailContract.Presenter {

    private final InjectOilDataSource mDataSource;
    private final InjectDetailContract.View mView;
    private CompositeSubscription subscription;

    InjectDetailPresenter(InjectOilDataSource mDataSource, InjectDetailContract.View mView) {
        this.mDataSource = mDataSource;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void getInjectDetailList(Map<String, String> map) {
        mView.showLoading();
        subscription.add(mDataSource.getInjectEquipLogList(map, new IListCallBack<InjectEquipmentLog.ItemList>() {
            @Override
            public void onSuccess(@NonNull List<InjectEquipmentLog.ItemList> list) {
                mView.showInjectDetailData(list);
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
    public void getInjectDetailListMore(Map<String, String> map) {
        subscription.add(mDataSource.getInjectEquipLogList(map, new IListCallBack<InjectEquipmentLog.ItemList>() {
            @Override
            public void onSuccess(@NonNull List<InjectEquipmentLog.ItemList> list) {
                mView.showMoreInjectDetailData(list);
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

    @Override
    public void subscribe() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }
}
