package com.sito.evpro.inspection.view.repair.overhaul.work;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.bean.overhaul.RepairWorkBean;
import com.sito.evpro.inspection.mode.bean.overhaul.WorkBean;
import com.sito.evpro.inspection.mode.inspection.InspectionDataSource;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import org.json.JSONObject;

import rx.subscriptions.CompositeSubscription;

/**
 * 检修工作P
 * Created by zhangan on 2017-06-26.
 */

class OverhaulWorkPresenter implements OverhaulWorkContract.Presenter {


    private final InspectionRepository mRepository;
    private final OverhaulWorkContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    OverhaulWorkPresenter(InspectionRepository mRepository, OverhaulWorkContract.View mView) {
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
    public void getRepairWork(String repairId) {
        mView.showLoading();
        mSubscriptions.add(mRepository.getRepairWork(repairId, new IObjectCallBack<OverhaulBean>() {
            @Override
            public void onSuccess(@NonNull OverhaulBean repairWorkBean) {
                mView.showRepairWork(repairWorkBean);
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
    public void loadRepairWorkFromDb(String repairId) {
        mSubscriptions.add(mRepository.loadRepairWorkFromDb(repairId, new IObjectCallBack<WorkBean>() {
            @Override
            public void onSuccess(@NonNull WorkBean workBean) {
                mView.showWorkData(workBean);
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void uploadAllData(JSONObject jsonObject) {
        mView.uploadProgress();
        mSubscriptions.add(mRepository.uploadOverhaulData(jsonObject, new InspectionDataSource.UploadRepairDataCallBack() {
            @Override
            public void uploadSuccess() {
                mView.uploadAllDataSuccess();
            }

            @Override
            public void uploadFail() {
                mView.uploadAllDataFail();
            }

        }));
    }

    @Override
    public void uploadImage(int workType, long itemId, String businessType, Image image) {
        mSubscriptions.add(mRepository.uploadImageFile(workType, businessType, itemId, image, new UploadImageCallBack() {
            @Override
            public void onSuccess() {
                mView.uploadImageSuccess();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError(Image image) {
                mView.uploadImageFail(image);
            }
        }));
    }

    @Override
    public void uploadVoiceFile(@NonNull String filePath, @NonNull String businessType, @NonNull String fileType) {
        mSubscriptions.add(mRepository.uploadFile(filePath, businessType, fileType, new InspectionDataSource.UploadFileCallBack() {

            @Override
            public void uploadSuccess(String url) {
                mView.uploadVoiceSuccess(url);
            }

            @Override
            public void uploadFail() {
                mView.uploadVoiceFail();
            }
        }));
    }
}
