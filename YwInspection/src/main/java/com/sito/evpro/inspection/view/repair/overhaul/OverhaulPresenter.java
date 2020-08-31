package com.sito.evpro.inspection.view.repair.overhaul;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-06-22.
 */

public class OverhaulPresenter implements OverhaulContract.Presenter {

    private final InspectionRepository mRepository;
    private final OverhaulContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public OverhaulPresenter(InspectionRepository mRepository, OverhaulContract.View mView) {
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
    public void getOverhaulList(@NonNull String time) {
        mSubscriptions.add(mRepository.getOverhaulList(time, new IListCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull List<OverhaulBean> list) {
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
    public void getOverhaulListMore(@NonNull String time, @NonNull String lastId) {
        mSubscriptions.add(mRepository.getOverhaulList(time, lastId, new IListCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull List<OverhaulBean> list) {
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

    @Override
    public void startOverhaul(final int position, long overhaulId) {
        mSubscriptions.add(mRepository.startOverhaul(overhaulId, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.startSuccess(position);
            }

            @Override
            public void onError(String message) {
                mView.startFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
