package com.sito.evpro.inspection.view.repair.overhaul.note;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017/8/24.
 */

public class OverhaulNotePresenter implements OverhaulContract.Presenter {

    private final InspectionRepository mRepository;
    private final OverhaulContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    OverhaulNotePresenter(InspectionRepository mRepository, OverhaulContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void getRepairState(@NonNull String repairId, @NonNull String jobId) {
        if (mRepository.getRepairState(repairId, jobId)) {
            mView.showOverhaulWork();
        } else {
            mView.showLayout();
            mView.showLoading();
            mSubscriptions.add(mRepository.getOverhaulNote(jobId, new IObjectCallBack<OverhaulNoteBean>() {
                @Override
                public void onSuccess(@NonNull OverhaulNoteBean overhaulNoteBean) {
                    mView.showOverhaulNote(overhaulNoteBean);
                }

                @Override
                public void onError(String message) {
                    mView.noData();
                }

                @Override
                public void onFinish() {
                    mView.hidLoading();
                }
            }));
        }
    }

    @Override
    public void setRepairState(@NonNull String repairId, @NonNull String jobId) {
        mRepository.setRepairState(repairId, jobId);
        mView.showOverhaulWork();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
