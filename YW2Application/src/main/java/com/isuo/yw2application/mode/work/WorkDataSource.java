package com.isuo.yw2application.mode.work;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.today.TodayToDoBean;
import com.isuo.yw2application.mode.bean.work.AwaitWorkBean;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.bean.work.InspectionDataBean;
import com.isuo.yw2application.mode.bean.work.WorkInspectionBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.bean.work.WorkState;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * 工作mode
 * Created by zhangan on 2017-07-14.
 */

public interface WorkDataSource {

    /**
     * 获取代办任务列表
     *
     * @param date     date
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getAwaitWorkData(@Nullable String date, @Nullable String lastId, @NonNull IListCallBack<AwaitWorkBean> callBack);

    /**
     * 获取增值工作列表
     *
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getIncrementData(String time, boolean isFinish, @Nullable String lastId, @NonNull IListCallBack<IncrementBean> callBack);

    /**
     * 获取巡检列表
     *
     * @param data     时间
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInspectionData(int inspectionType, @NonNull String data, @Nullable String lastId, @NonNull IListCallBack<WorkInspectionBean> callBack);

    /**
     * 获取巡检列表
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInspectionData(JSONObject jsonObject, @NonNull IListCallBack<WorkInspectionBean> callBack);

    /**
     * 获取检修列表
     *
     * @param data     时间
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getOverhaulData(@NonNull String data, @Nullable String lastId, @NonNull IListCallBack<OverhaulBean> callBack);

    /**
     * 获取检修详情
     *
     * @param repairId 维修id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getRepairDetail(@NonNull String repairId, @NonNull IObjectCallBack<OverhaulBean> callBack);

    @NonNull
    Subscription getInspectionData(@NonNull Long taskId, @NonNull IObjectCallBack<InspectionDataBean> callBack);

    @NonNull
    Subscription getIncrement(@NonNull Long taskId, @NonNull IObjectCallBack<IncrementBean> callBack);


    interface WorkItemCallBack {

        void showWorkItem(List<WorkItem> workItems);

    }

    interface WorkItemAllCallBack extends WorkItemCallBack {

        void showAllWorkItem(List<WorkItem> workItems);

        void showPayWorkItem(List<WorkItem> workItems);

    }

    void getWorkItems(WorkItemCallBack callBack);

    void getAllWorkItems(WorkItemAllCallBack callBack);

    void saveWorkItems(List<WorkItem> items);

    @NonNull
    Subscription getWorkState(IObjectCallBack<WorkState> callBack);

    @NonNull
    Subscription get24HFaultList(Map<String, String> map, @NonNull IListCallBack<FaultList> callBack);
}
