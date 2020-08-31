package com.sito.customer.mode.work;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.bean.overhaul.FaultBean;
import com.sito.customer.mode.bean.today.TodayToDoBean;
import com.sito.customer.mode.bean.work.AwaitWorkBean;
import com.sito.customer.mode.bean.work.IncrementBean;
import com.sito.customer.mode.bean.work.InspectionBean;
import com.sito.customer.mode.bean.work.InspectionDataBean;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.bean.work.WorkItem;
import com.sito.customer.mode.bean.work.WorkState;

import java.util.List;

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
    Subscription getInspectionData(int inspectionType, @NonNull String data, @Nullable String lastId, @NonNull IListCallBack<InspectionBean> callBack);

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
     * 领取任务
     *
     * @param taskId   任务id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getOperationTask(@NonNull String taskId, @NonNull IObjectCallBack<String> callBack);

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

        void showAllWorkItem(List<WorkItem> workItems);
    }

    void getWorkItems(WorkItemCallBack callBack);

    void saveWorkItems(List<WorkItem> items);

    @NonNull
    Subscription getWorkState(IObjectCallBack<WorkState> callBack);

    @NonNull
    Subscription getTodayToList(@NonNull IListCallBack<TodayToDoBean> callBack);

    @NonNull
    Subscription getTodayFaultList(boolean isRemain, String time, @NonNull IListCallBack<FaultList> callBack);
}
