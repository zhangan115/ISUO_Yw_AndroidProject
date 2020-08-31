package com.sito.customer.mode.equipment;

import android.support.annotation.NonNull;


import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.check.CheckValue;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.bean.check.InspectionData;
import com.sito.customer.mode.bean.equip.EquipRecordDetail;
import com.sito.customer.mode.bean.equip.TimeLineBean;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;

import rx.Subscription;

/**
 * 设备相关
 * Created by zhangan on 2017/10/13.
 */

public interface EquipmentDataSource {

    //根据equipId获取检修记录
    Subscription getOverByEId(long equipId, @NonNull IListCallBack<OverhaulBean> callBack);

    Subscription getMoreOverByEId(long equipId, int lastId, @NonNull IListCallBack<OverhaulBean> callBack);

    //根据equipId获取巡检数据
    Subscription getCheckData(long equipId, @NonNull IObjectCallBack<InspectionData> callBack);

    Subscription getCheckValue(long equipId, int inspecId, @NonNull IObjectCallBack<CheckValue> callBack);

    Subscription getFaultByEId(long equipId, IListCallBack<FaultList> callBack);

    Subscription getMoreFaultByEId(long equipId, int lastId, IListCallBack<FaultList> callBack);

    @NonNull
    Subscription getEquipRepairRecordData(long equipmentId, IListCallBack<TimeLineBean> callBack);

    @NonNull
    Subscription getEquipExperimentData(long equipmentId, IListCallBack<TimeLineBean> callBack);

    @NonNull
    Subscription getEquipCheckData(long equipmentId, IListCallBack<TimeLineBean> callBack);

    @NonNull
    Subscription getEquipRecordData(long equipmentRecordId, IObjectCallBack<EquipRecordDetail> callBack);

    void downLoadFile(String fileName, String filePath, String downLoadUrl, @NonNull DownLoadCallBack callBack);


    interface DownLoadCallBack {

        void onSuccess(String fileName, String filePath);

        void onFile();
    }
}
