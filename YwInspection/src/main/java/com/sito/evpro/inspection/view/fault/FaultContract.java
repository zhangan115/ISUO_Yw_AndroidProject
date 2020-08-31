package com.sito.evpro.inspection.view.fault;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.fault.DefaultFlowBean;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;
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

        /**
         * 获取预设流程
         */
        void getUserFlowList();

        /**
         * 上传语音文件
         *
         * @param workType
         * @param businessType
         */
        void postVoiceFile(int workType, String businessType);

        void postFaultInfo(JSONObject jsonObject);

        /**
         * 上传完成清理数据
         */
        void uploadSuccess(Long defaultFlowId);

        /**
         * 上传照片数据
         *
         * @param image 图片
         */
        void uploadImage(int workType, String businessType, Image image);

    }

    interface View extends BaseView<Presenter> {

        /**
         * 提交故障成功
         *
         * @param result
         */
        void postSuccess(UploadResult result);

        /**
         * 提交语音成功
         *
         * @param fileBeen
         */
        void postVoiceSuccess(List<String> fileBeen);

        /**
         * 提交失败
         */
        void postFail(String message);

        /**
         * 提交完成
         */
        void postFinish();

        void showLoading();

        void hideLoading();

        /**
         * 显示预设流程
         *
         * @param defaultFlowBeen
         */
        void showDefaultFlowList(@NonNull List<DefaultFlowBean> defaultFlowBeen);

        /**
         * 预设流程失败
         */
        void showDefaultFlowError();

        void uploadImageSuccess();

        void uploadImageFail(Image image);
    }

}