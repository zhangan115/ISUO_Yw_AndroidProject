package com.isuo.yw2application.view.main.task.increment;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.work.WorkRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-22.
 */

public class IncrementPresenter implements IncrementContract.Presenter {

    private CompositeSubscription mSubscriptions;
    private final WorkRepository mRepository;
    private final IncrementContract.View mView;

    IncrementPresenter(WorkRepository mRepository, IncrementContract.View mView) {
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
    public void getData(String time, boolean isFinish) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getIncrementData(time, isFinish, null, new IListCallBack<IncrementBean>() {
            @Override
            public void onSuccess(@NonNull List<IncrementBean> list) {
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
    public void getDataMore(String time, boolean isFinish, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getIncrementData(time, isFinish, lastId, new IListCallBack<IncrementBean>() {
            @Override
            public void onSuccess(@NonNull List<IncrementBean> list) {
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
