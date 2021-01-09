package com.isuo.yw2application.mode.fault;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.mode.bean.fault.DefaultFlowBean;
import com.isuo.yw2application.mode.bean.fault.FaultCount;
import com.isuo.yw2application.mode.bean.fault.FaultDetail;
import com.isuo.yw2application.mode.bean.fault.JobPackageBean;

import org.json.JSONObject;

import java.util.Map;

import rx.Subscription;

/**
 * 故障data source
 * Created by zhangan on 2017-07-17.
 */

public interface FaultDataSource {

    @NonNull
    Subscription getEquipmentType(@NonNull IListCallBack<EquipType> callBack);

    @NonNull
    Subscription getFaultList(@Nullable String equipmentType, @Nullable String alarmType, @Nullable String alarmState
            , @Nullable String startTimeStr, @Nullable String endTimeStr, @Nullable String lastId, @NonNull IListCallBack<FaultList> callBack);

    @NonNull
    Subscription getFaultDetail(String faultId, @NonNull IObjectCallBack<FaultDetail> callBack);

    @NonNull
    Subscription getEmployeeList(Integer type, @NonNull IListCallBack<DepartmentBean> callBack);

    @NonNull
    Subscription closeFault(Map<String, String> map, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription flowFault(Map<String, String> map, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription overhaulFault(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getJobPackage(@NonNull IObjectCallBack<StandBean> callBack);

    @NonNull
    Subscription postFaultInfo(JSONObject jsonObject, @NonNull IObjectCallBack<UploadResult> callBack);

    @NonNull
    Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId
            , @NonNull Image image, @NonNull UploadImageCallBack callBack);

    @NonNull
    Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull IListCallBack<String> callBack);

    @NonNull
    Subscription getDefaultFlow(@NonNull IListCallBack<DefaultFlowBean> callBack);

    @NonNull
    Subscription getHistoryList(int userId,int count, @NonNull IListCallBack<FaultList> callBack);

    @NonNull
    Subscription getMoreHistoryList(int userId,int count, long lastId, @NonNull IListCallBack<FaultList> callBack);

    @NonNull
    Subscription careEquipment(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getFaultCount(@NonNull IObjectCallBack<FaultCount> callBack);
}
