package com.isuo.yw2application.view.main.task.overhaul.execute;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.overhaul.WorkBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

/**
 * 检修工作
 * Created by zhangan on 2017-06-26.
 */

interface OverhaulExecuteContract {

    interface Presenter extends BasePresenter {

        void getRepairWork(String repairId);

        void loadRepairWorkFromDb(String repairId);

        void uploadAllData(JSONObject jsonObject);

        void uploadImage(int workType, long itemId, String businessType, Image image);

        void uploadVoiceFile(@NonNull String filePath, @NonNull String businessType, @NonNull String fileType);

        void startRepair(String repairId);
    }


    interface View extends BaseView<Presenter> {

        void showRepairWork(OverhaulBean repairWorkBean);

        void showLoading();

        void hideLoading();

        void noData();

        void showWorkData(WorkBean workBean);

        void uploadVoiceSuccess(String url);

        void uploadVoiceFail();

        void uploadAllDataSuccess();

        void uploadAllDataFail();

        void uploadProgress();

        void uploadImageSuccess();

        void uploadImageFail(Image image);

    }
}
