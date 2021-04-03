package com.isuo.yw2application.view.main.alarm.fault;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.fault.DefaultFlowBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * 故障上报
 * Created by Administrator on 2017/6/25.
 */
interface FaultContract {

    interface Presenter extends BasePresenter {

        void getUserFlowList();

        void postVoiceFile(int workType, String businessType);

        void postFaultInfo(JSONObject jsonObject);

        void uploadSuccess(Long defaultFlowId);

        void uploadImage(int workType, String businessType, Image image);

    }

    interface View extends BaseView<Presenter> {

        void postSuccess(UploadResult result);

        void postVoiceSuccess(List<String> fileBeen);

        void postFail(String message);

        void postFinish();

        void showLoading();

        void hideLoading();

        void showDefaultFlowList(@NonNull List<DefaultFlowBean> defaultFlowBeen);

        void showDefaultFlowError();

        void uploadImageSuccess();

        void uploadImageFail(Image image);
    }

}