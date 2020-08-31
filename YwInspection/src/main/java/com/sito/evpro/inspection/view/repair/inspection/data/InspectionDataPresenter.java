package com.sito.evpro.inspection.view.repair.inspection.data;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDataBean;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepository;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepositoryComponent;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检数据
 * Created by zhangan on 2017/9/27.
 */

class InspectionDataPresenter implements InspectionDataContract.Presenter {


    private final InspectionWorkRepository mRepository;
    private final InspectionDataContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @Inject
    InspectionDataPresenter(InspectionWorkRepository mRepository, InspectionDataContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getInspectionData(Long taskId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionData(taskId, new IObjectCallBack<InspectionDataBean>() {
            @Override
            public void onSuccess(@NonNull InspectionDataBean dataBean) {
                mView.showInspectionData(dataBean);
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
}
