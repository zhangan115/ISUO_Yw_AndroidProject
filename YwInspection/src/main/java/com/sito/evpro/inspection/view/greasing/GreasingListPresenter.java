package com.sito.evpro.inspection.view.greasing;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectResultBean;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 注油管理
 * Created by Administrator on 2017/6/22.
 */
final class GreasingListPresenter implements GreasingListContract.Presenter {
    private InspectionRepository mRepository;
    private GreasingListContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private List<InjectEquipment> needInjectEqu;

    @Inject
    GreasingListPresenter(InspectionRepository repository, GreasingListContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
        needInjectEqu = new ArrayList<>();
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
    public void searchEquipment(List<InjectEquipment> injectEquipments, String searchText) {
        needInjectEqu.clear();
        for (int i = 0; i < injectEquipments.size(); i++) {
            InjectEquipment data = injectEquipments.get(i);
            if (!TextUtils.isEmpty(data.getEquipmentSn())) {
                if (data.getEquipmentSn().contains(searchText) || data.getEquipmentName().contains(searchText)) {
                    needInjectEqu.add(data);
                }
            } else {
                if (data.getEquipmentName().contains(searchText)) {
                    needInjectEqu.add(data);
                }
            }
        }
        if (TextUtils.isEmpty(searchText)) {
            mView.showNeedInjectEqu(injectEquipments);
        } else {
            mView.showNeedInjectEqu(needInjectEqu);
        }
    }

    @Override
    public void getRoomList() {
        mSubscriptions.add(mRepository.getInjectRoomList(1, new IListCallBack<InjectRoomBean>() {
            @Override
            public void onSuccess(@NonNull List<InjectRoomBean> list) {
                mView.showRoomList(list);
            }

            @Override
            public void onError(String message) {
                mView.getRoomListError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getRoomEquipmentList(long roomId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInjectEquipmentList(roomId, new IListCallBack<InjectEquipment>() {
            @Override
            public void onSuccess(@NonNull List<InjectEquipment> list) {
                mView.showRoomEquipment(list);
            }

            @Override
            public void onError(String message) {
                mView.getEquipmentError();
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void injectionEquipment(long equipmentId, Integer cycle, final int position) {
        mSubscriptions.add(mRepository.injectEquipmentList(equipmentId, cycle, new IObjectCallBack<InjectResultBean>() {
            @Override
            public void onSuccess(@NonNull InjectResultBean resultBean) {
                mView.injectSuccess(position);
            }

            @Override
            public void onError(String message) {
                mView.injectFail(position);
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void getNeedInjectEqu(List<InjectEquipment> injectEquipments) {
        needInjectEqu.clear();
        for (int i = 0; i < injectEquipments.size(); i++) {
            InjectEquipment data = injectEquipments.get(i);
            if (data.getInjectionOil() == null) {
                needInjectEqu.add(data);
            } else {
                long time = System.currentTimeMillis() - data.getInjectionOil().getCreateTime();
                if (time > data.getCycle() * 24L * 60L * 60L * 1000L) {
                    needInjectEqu.add(data);
                }
            }
        }
        mView.showNeedInjectEqu(needInjectEqu);
    }
}
