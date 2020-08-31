package com.sito.customer.view.home.work.sos;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.sos.EmergencyCall;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * sos
 * Created by zhangan on 2017/8/23.
 */

class SOSContract {

    interface View extends BaseView<Presenter> {

        void showEmergencyCalls(List<EmergencyCall> emergencyCalls);

        void emergencyCallError();

        void uploadDataSuccess();

        void uploadDataFail(String message);

        void showUploadProgress();

        void hideUploadProgress();

        void uploadVoiceSuccess(String url);

        void uploadVoiceFail();
    }

    interface Presenter extends BasePresenter {

        void emergencyCalls();

        void uploadEmergencyData(JSONObject jsonObject);

        void uploadVoiceFile(@NonNull String voiceLocalPath, String businessType, String fileType);
    }
}
