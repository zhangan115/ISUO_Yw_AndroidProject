package com.isuo.yw2application.view.main.alarm.fault;

import android.support.annotation.NonNull;

import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.ImageDao;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.bean.db.VoiceDao;
import com.isuo.yw2application.mode.bean.fault.DefaultFlowBean;
import com.isuo.yw2application.mode.fault.FaultDataSource;

import org.json.JSONObject;

import java.util.List;

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
        List<Image> images = Yw2Application.getInstance().getDaoSession().getImageDao().queryBuilder()
                .where(ImageDao.Properties.WorkType.eq(ConstantInt.FAULT)
                        , ImageDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())).list();
        Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(images);
        //更新录音为已提交
        List<Voice> voices = Yw2Application.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.FAULT)
                        , VoiceDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())).list();
        Yw2Application.getInstance().getDaoSession().getVoiceDao().deleteInTx(voices);
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
