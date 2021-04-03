package com.isuo.yw2application.view.main.task.inspection.data;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.work.InspectionDataBean;
import com.isuo.yw2application.mode.work.WorkRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * 巡检数据
 * Created by zhangan on 2017/9/27.
 */

class InspectionDataPresenter implements InspectionDataContract.Presenter {


    private final WorkRepository mRepository;
    private final InspectionDataContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InspectionDataPresenter(WorkRepository mRepository, InspectionDataContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
        setUpListeners();
    }

    private void setUpListeners() {
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
