package com.isuo.yw2application.view.main.alarm.detail;

import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.mode.bean.fault.FaultDetail;
import com.isuo.yw2application.mode.bean.fault.JobPackageBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.Map;

/**
 * 故障流转
 * Created by zhangan on 2017-07-18.
 */

interface AlarmDetailContract {

    interface Presenter extends BasePresenter {

        void getFaultDetailData(String faultId);

        void closeFault(Map<String, String> map);

        void overhaulFault(JSONObject jsonObject);

        void uploadFaultInfo(Map<String, String> map);

        void getJobPackage();

        void careEquipment(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void showData(FaultDetail faultDetail);

        void showLoading();

        void hideLoading();

        void uploadSuccess();

        void uploadFail();

        void uploadProgress();

        void showJobPackage(StandBean jobPackageBeen);

        void careSuccess();

        void careFail();
    }
}
