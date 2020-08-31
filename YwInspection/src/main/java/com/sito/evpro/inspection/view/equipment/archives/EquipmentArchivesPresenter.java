package com.sito.evpro.inspection.view.equipment.archives;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

public class EquipmentArchivesPresenter implements EquipmentArchivesContract.Presenter {

    private final InspectionRepository mRepository;
    private final EquipmentArchivesContract.View mView;
    @NonNull
    private CompositeSubscription subscription;

    @Inject
    EquipmentArchivesPresenter(InspectionRepository mRepository, EquipmentArchivesContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        subscription = new CompositeSubscription();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscription.clear();
    }

    @Override
    public void getEquipmentDetail(long equipmentId) {
        subscription.add(mRepository.getEquipmentDetail(equipmentId, new IObjectCallBack<EquipmentBean>() {
            @Override
            public void onSuccess(@NonNull EquipmentBean bean) {
                mView.showEquipmentDetail(bean);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
