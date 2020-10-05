package com.isuo.yw2application.view.main.generate.increment;

import com.isuo.yw2application.mode.bean.db.Image;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

/**
 * 生成增值工作
 * Created by zhangan on 2017/9/29.
 */

interface GenerateIncrementContract {

    interface Presenter extends BasePresenter {
        /**
         * 上传单张照片
         *
         * @param businessType
         * @param path
         */
        void uploadImage(String businessType, Image path);

        /**
         * 重新上传照片
         *
         * @param position
         * @param businessType
         * @param path
         */
        void uploadImage(int position, String businessType, String path);


        /**
         * 上传语音文件
         *
         * @param businessType
         * @param path
         */
        void uploadVoiceFile(String businessType, String path);

        /**
         * 删除文件
         *
         * @param voicePath 路径
         */
        void deleteVoiceFile(String voicePath);


        void uploadAllData(JSONObject uploadJson);
    }

    interface View extends BaseView<Presenter> {

        void uploadImgSuccess();

        void uploadImgFail(Image image);

        void uploadImgSuccess(int position, Image image);

        void showErrorMessage(String message);

        void uploadVoiceSuccess(String voiceUrl);

        void uploadDataSuccess();

        void showUploadProgress();

        void hideUploadProgress();
    }

}
