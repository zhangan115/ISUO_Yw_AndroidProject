package com.isuo.yw2application.view.main.equip.time.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.equip.EquipRecordDetail;
import com.isuo.yw2application.mode.equipment.EquipmentDataSource;
import com.isuo.yw2application.mode.equipment.EquipmentRepository;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * 检测详情,大修详情,实验详情
 * Created by zhangan on 2017/10/13.
 */

class RecordDetailPresenter implements EquipmentRecordDetailContract.Presenter {


    private final EquipmentRepository mRepository;
    private final EquipmentRecordDetailContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    RecordDetailPresenter(EquipmentRepository mRepository, EquipmentRecordDetailContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getRecordDetail(long equipmentRecordId) {
        mView.showLoading();
        mSubscription.add(mRepository.getEquipRecordData(equipmentRecordId, new IObjectCallBack<EquipRecordDetail>() {
            @Override
            public void onSuccess(@NonNull EquipRecordDetail equipRecordDetail) {
                mView.showData(equipRecordDetail);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void downLoadFile(String url, String savePath, String fileName) {
        mView.showDownLoadProgress();
        mRepository.downLoadFile(fileName, savePath, url, new EquipmentDataSource.DownLoadCallBack() {
            @Override
            public void onSuccess(String fileName, String filePath) {
                mView.hideDownProgress();
                mView.downLoadSuccess(new File(filePath,fileName));
            }

            @Override
            public void onFile() {
                mView.hideDownProgress();
                mView.downLoadFail();
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }
}
