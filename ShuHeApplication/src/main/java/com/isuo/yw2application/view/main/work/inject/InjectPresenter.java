package com.isuo.yw2application.view.main.work.inject;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.inject.InjectOilDataSource;
import com.isuo.yw2application.mode.inject.bean.InjectEquipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * 润油管理
 * Created by zhangan on 2017/9/21.
 */

class InjectPresenter implements InjectContract.Presenter {

    private InjectOilDataSource mRepository;
    private InjectContract.View mView;
    private CompositeSubscription mSubscriptions;
    private List<InjectEquipment> needInjectEqu;

    InjectPresenter(InjectOilDataSource repository, InjectContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
        needInjectEqu = new ArrayList<>();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void getRoomEquipmentList(Map<String, String> map) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getInjectEquipmentList(map, new IListCallBack<InjectEquipment>() {
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
            mView.showSearchInjectEqu(injectEquipments);
        } else {
            if (needInjectEqu.size() == 0) {
                mView.noData();
            } else {
                mView.showSearchInjectEqu(needInjectEqu);
            }
        }
    }

}
