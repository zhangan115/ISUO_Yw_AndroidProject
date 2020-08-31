package com.sito.evpro.inspection.view.energy;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/22.
 */
final class EnergyListPresenter implements EnergyListContract.Presenter {
    private InspectionRepository mRepository;
    private EnergyListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    EnergyListPresenter(InspectionRepository repository, EnergyListContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getEnergyInfo() {
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }
}
