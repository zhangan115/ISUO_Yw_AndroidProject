package com.sito.evpro.inspection.view.repair.overhaul.work;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.bean.overhaul.RepairWorkBean;
import com.sito.evpro.inspection.mode.bean.overhaul.WorkBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

/**
 * 检修工作
 * Created by zhangan on 2017-06-26.
 */

interface OverhaulWorkContract {

    interface Presenter extends BasePresenter {

        void getRepairWork(String repairId);

        void loadRepairWorkFromDb(String repairId);

        void uploadAllData(JSONObject jsonObject);

        /**
         * 上传照片数据
         *
         * @param image 图片
         */
        void uploadImage(int workType, long itemId, String businessType, Image image);

        void uploadVoiceFile(@NonNull String filePath, @NonNull String businessType, @NonNull String fileType);
    }


    interface View extends BaseView<Presenter> {

        void showRepairWork(OverhaulBean repairWorkBean);

        void showLoading();

        void hideLoading();

        void noData();

        void showWorkData(WorkBean workBean);

        void uploadImageFail();

        void uploadVoiceSuccess(String url);

        void uploadVoiceFail();

        void uploadAllDataSuccess();

        void uploadAllDataFail();

        void uploadProgress();

        void uploadImageSuccess();

        void uploadImageFail(Image image);

    }
}
