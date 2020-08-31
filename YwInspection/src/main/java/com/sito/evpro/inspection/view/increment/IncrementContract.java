package com.sito.evpro.inspection.view.increment;

import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * 增值工作协议
 * Created by zhangan on 2017/6/25.
 */
interface IncrementContract {

    interface Presenter extends BasePresenter {

        void postVoiceFile(int workType, String businessType);

        void uploadSuccess();

        /**
         * 上传照片数据
         *
         * @param image 图片
         */
        void uploadImage(int workType, String businessType, Image image);

        void postIncrementInfo(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void postSuccess(UploadResult result);

        void postImageSuccess(List<String> fileBeen);

        void postVoiceSuccess(List<String> fileBeen);

        void postFail();

        void postFinish();

        void showLoading();

        void hideLoading();

        void postSingleImgSuccess(Image image);

        void postSingleVocSuccess(List<String> lists);

        void uploadImageSuccess();

        void uploadImageFail(Image image);
    }

}