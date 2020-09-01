package com.isuo.yw2application.mode.overhaul;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulNoteBean;
import com.isuo.yw2application.mode.bean.overhaul.WorkBean;

import org.json.JSONObject;

import rx.Subscription;

/**
 * 巡检逻辑
 * Created by zhangan on 2018/3/20.
 */

public interface OverhaulSourceData {

    @NonNull
    Subscription getSecureInfo(long securityId, @NonNull IObjectCallBack<OverhaulNoteBean> callBack);

    @NonNull
    Subscription startRepair(String repairId);

    @NonNull
    Subscription getRepairWork(@NonNull String repairId, @NonNull IObjectCallBack<OverhaulBean> callBack);

    @NonNull
    Subscription loadRepairWorkFromDb(@NonNull String repairId, @NonNull IObjectCallBack<WorkBean> callBack);

    interface UploadRepairDataCallBack {

        void uploadSuccess();

        void uploadFail();
    }

    @NonNull
    Subscription uploadOverhaulData(@NonNull JSONObject jsonObject, @NonNull UploadRepairDataCallBack callBack);

    @NonNull
    Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId
            , @NonNull Image image, @NonNull UploadImageCallBack callBack);

    interface UploadFileCallBack {

        void uploadSuccess(String url);

        void uploadFail();
    }

    @NonNull
    Subscription uploadFile(@NonNull String filePath, @NonNull String businessType
            , @NonNull String fileType, @NonNull UploadFileCallBack callBack);
}
