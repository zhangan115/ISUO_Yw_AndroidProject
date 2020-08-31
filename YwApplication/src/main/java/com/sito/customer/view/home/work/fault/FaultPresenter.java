package com.sito.customer.view.home.work.fault;

import android.support.annotation.NonNull;


import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.UploadResult;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.ImageDao;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.customer.mode.bean.db.VoiceDao;
import com.sito.customer.mode.bean.fault.DefaultFlowBean;
import com.sito.customer.mode.fault.FaultDataSource;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * 故障上报
 * Created by Administrator on 2017/6/25.
 */
final class FaultPresenter implements FaultContract.Presenter {
    private FaultDataSource mRepository;
    private FaultContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    FaultPresenter(FaultDataSource repository, FaultContract.View view) {
        this.mRepository = repository;
        this.mView = view;
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
    public void postFaultInfo(JSONObject jsonObject) {
        mSubscriptions.add(mRepository.postFaultInfo(jsonObject, new IObjectCallBack<UploadResult>() {
            @Override
            public void onSuccess(@NonNull UploadResult uploadResult) {
                mView.postSuccess(uploadResult);
            }

            @Override
            public void onError(String message) {
                mView.postFail(message);
            }

            @Override
            public void onFinish() {
                mView.postFinish();
            }
        }));
    }

    @Override
    public void uploadSuccess(Long defaultFlowId) {
        //更新图片状态为已提交
        List<Image> images = CustomerApp.getInstance().getDaoSession().getImageDao().queryBuilder()
                .where(ImageDao.Properties.WorkType.eq(ConstantInt.FAULT)
                        , ImageDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())).list();
        CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(images);
        //更新录音为已提交
        List<Voice> voices = CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.FAULT)
                        , VoiceDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())).list();
        CustomerApp.getInstance().getDaoSession().getVoiceDao().deleteInTx(voices);
    }

    @Override
    public void uploadImage(int workType, String businessType, Image image) {
        mSubscriptions.add(mRepository.uploadImageFile(workType, businessType, null, image, new UploadImageCallBack() {
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
    public void postVoiceFile(int workType, String businessType) {
        mSubscriptions.add(mRepository.postIncrementVoiceFile(workType, businessType, new IListCallBack<String>() {
            @Override
            public void onSuccess(@NonNull List<String> list) {
                mView.postVoiceSuccess(list);
            }

            @Override
            public void onError(String message) {
                mView.postFail(message);
            }

            @Override
            public void onFinish() {
                mView.postFinish();
            }
        }));
    }


    @Override
    public void getUserFlowList() {
        mSubscriptions.add(mRepository.getDefaultFlow(new IListCallBack<DefaultFlowBean>() {
            @Override
            public void onSuccess(@NonNull List<DefaultFlowBean> list) {
                mView.showDefaultFlowList(list);
            }

            @Override
            public void onError(String message) {
                mView.showDefaultFlowError();
            }

            @Override
            public void onFinish() {

            }
        }));
    }
}
