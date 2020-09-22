package com.isuo.yw2application.mode.work;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.isuo.yw2application.R;
import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.api.FaultApi;
import com.isuo.yw2application.api.WorkApi;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.today.TodayToDoBean;
import com.isuo.yw2application.mode.bean.work.AwaitWorkBean;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.bean.work.InspectionDataBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.bean.work.WorkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscription;

/**
 * 工作mode
 * Created by zhangan on 2017-07-14.
 */
@Singleton
public class WorkRepository implements WorkDataSource {

    private SharedPreferences sp;

    @Inject
    public WorkRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public Subscription getAwaitWorkData(@Nullable String date, @Nullable String lastId
            , @NonNull final IListCallBack<AwaitWorkBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("count", ConstantInt.PAGE_SIZE);
            jsonObject.put("lastId", lastId);
            jsonObject.put("flowTime", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<AwaitWorkBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getAwaitWork(jsonObject.toString());
        return new ApiCallBack<List<AwaitWorkBean>>(observable) {
            @Override
            public void onSuccess(List<AwaitWorkBean> been) {
                callBack.onFinish();
                if (been == null || been.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(been);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getIncrementData(String time, boolean isFinish, @Nullable String lastId
            , @NonNull final IListCallBack<IncrementBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("agentType", 1);
            jsonObject.put("lastId", lastId);
            if (!TextUtils.isEmpty(time)) {
                jsonObject.put("time", time);
                jsonObject.put("count", ConstantInt.MAX_PAGE_SIZE);
            } else {
                jsonObject.put("count", ConstantInt.PAGE_SIZE);
            }
            if (isFinish) {
                jsonObject.put("legacyTime", 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<IncrementBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getIncrement(jsonObject.toString());
        return new ApiCallBack<List<IncrementBean>>(observable) {
            @Override
            public void onSuccess(List<IncrementBean> been) {
                callBack.onFinish();
                if (been == null || been.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(been);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getInspectionData(int inspectionType, @NonNull String data, @Nullable String lastId
            , @NonNull final IListCallBack<InspectionBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", data);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", ConstantInt.MAX_PAGE_SIZE);
            if (inspectionType != -1) {
                switch (inspectionType) {
                    case 1:
                        jsonObject.put("planPeriodType", 1);
                        break;
                    case 2:
                        jsonObject.put("planPeriodType", 2);
                        break;
                    case 3:
                        jsonObject.put("planPeriodType", 3);
                        break;
                }
            }
            jsonObject.put("lastId", lastId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<InspectionBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getInspection(jsonObject.toString());
        return new ApiCallBack<List<InspectionBean>>(observable) {
            @Override
            public void onSuccess(List<InspectionBean> been) {
                callBack.onFinish();
                if (been == null || been.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(been);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getOverhaulData(@NonNull String data, @Nullable String lastId, @NonNull final IListCallBack<OverhaulBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", data);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", ConstantInt.MAX_PAGE_SIZE);
            jsonObject.put("lastId", lastId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<OverhaulBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getOverhaul(jsonObject.toString());
        return new ApiCallBack<List<OverhaulBean>>(observable) {
            @Override
            public void onSuccess(List<OverhaulBean> been) {
                callBack.onFinish();
                if (been == null || been.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(been);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getOperationTask(@NonNull String taskId, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable =
                Api.createRetrofit().create(WorkApi.class).operationTask(taskId, ConstantInt.OPERATION_STATE_1);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String s) {
                callBack.onFinish();
                callBack.onSuccess(s);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getRepairDetail(@NonNull String repairId, @NonNull final IObjectCallBack<OverhaulBean> callBack) {
        Observable<Bean<OverhaulBean>> observable = Api.createRetrofit().create(WorkApi.class).getRepairDetail(repairId);
        return new ApiCallBack<OverhaulBean>(observable) {
            @Override
            public void onSuccess(@Nullable OverhaulBean repairWorkBean) {
                callBack.onFinish();
                if (repairWorkBean != null) {
                    callBack.onSuccess(repairWorkBean);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getInspectionData(@NonNull Long taskId, @NonNull final IObjectCallBack<InspectionDataBean> callBack) {
        Observable<Bean<InspectionDataBean>> observable = Api.createRetrofit().create(WorkApi.class).getInspectionData(taskId);
        return new ApiCallBack<InspectionDataBean>(observable) {
            @Override
            public void onSuccess(@Nullable InspectionDataBean dataBean) {
                callBack.onFinish();
                if (dataBean != null && dataBean.getRoomList() != null && dataBean.getRoomList().size() > 0) {
                    callBack.onSuccess(dataBean);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getIncrement(@NonNull Long taskId, @NonNull final IObjectCallBack<IncrementBean> callBack) {
        Observable<Bean<IncrementBean>> observable = Api.createRetrofit().create(WorkApi.class).getIncrementData(taskId);
        return new ApiCallBack<IncrementBean>(observable) {
            @Override
            public void onSuccess(@Nullable IncrementBean dataBean) {
                callBack.onFinish();
                if (dataBean != null) {
                    callBack.onSuccess(dataBean);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public void saveWorkItems(List<WorkItem> items) {
        StringBuilder sb = new StringBuilder();
        items.remove(items.size() - 1);
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).getId());
            if (i != items.size() - 1) {
                sb.append(",");
            }
        }
        sp.edit().putString(ConstantStr.WORK_ITEM, sb.toString()).apply();
    }

    @NonNull
    @Override
    public Subscription getWorkState(final IObjectCallBack<WorkState> callBack) {
        return new ApiCallBack<WorkState>(Api.createRetrofit().create(Api.Count.class).getWorkStat()) {
            @Override
            public void onSuccess(@Nullable WorkState workState) {
                callBack.onFinish();
                if (workState != null) {
                    callBack.onSuccess(workState);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getTodayToList(@NonNull final IListCallBack<TodayToDoBean> callBack) {
        return new ApiCallBack<List<TodayToDoBean>>(Api.createRetrofit().create(Api.Count.class).getTodayToList()) {
            @Override
            public void onSuccess(@Nullable List<TodayToDoBean> s) {
                callBack.onFinish();
                if (s != null && s.size() > 0) {
                    callBack.onSuccess(s);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getTodayFaultList(boolean isRemain, String time, @NonNull final IListCallBack<FaultList> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (TextUtils.isEmpty(time)) {
                jsonObject.put("legacyTime", 1);
                jsonObject.put("executeUser", 1);
            } else {
                jsonObject.put("startTime", time);
                jsonObject.put("endTime", time);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ApiCallBack<List<FaultList>>(Api.createRetrofit().create(FaultApi.class).getFaultList(jsonObject.toString())) {

            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public void getWorkItems(final WorkItemCallBack callBack) {
        String workItemStr = sp.getString(ConstantStr.WORK_ITEM, "");
        List<WorkItem> allWorkItems = new ArrayList<>();
        List<WorkItem> myWorkItems = new ArrayList<>();
        allWorkItems.add(new WorkItem(1, "专项工作", R.drawable.special));
        allWorkItems.add(new WorkItem(2, "检修工作", R.drawable.overhaul));
        allWorkItems.add(new WorkItem(3, "指派检修", R.drawable.assign));
        allWorkItems.add(new WorkItem(4, "发布通知", R.drawable.notice));
        allWorkItems.add(new WorkItem(5, "故障上报", R.drawable.fault_report));
        allWorkItems.add(new WorkItem(6, "台账录入", R.drawable.standing_book));
        allWorkItems.add(new WorkItem(7, "企业规范", R.drawable.standard));
        // TODO: 2020/9/22  
//        allWorkItems.add(new WorkItem(8, "指派检修", R.drawable.bg_home_icon_zp));
//        allWorkItems.add(new WorkItem(9, "工具管理", R.drawable.bg_home_icon_gj));
//        allWorkItems.add(new WorkItem(10, "发布通知", R.drawable.bg_home_icon_fb));
//        allWorkItems.add(new WorkItem(11, "台账录入", R.drawable.bg_home_icon_tz));
        if (TextUtils.isEmpty(workItemStr)) {
            for (int i = 0; i < 7; i++) {
                myWorkItems.add(allWorkItems.get(i));
            }
            myWorkItems.add(new WorkItem(-1, "全部", R.drawable.all));
        } else {
            String[] saveItems = workItemStr.split(",");
            for (String saveItem : saveItems) {
                for (int j = 0; j < allWorkItems.size(); j++) {
                    int id = Integer.valueOf(saveItem);
                    if (id == allWorkItems.get(j).getId()) {
                        myWorkItems.add(allWorkItems.get(j));
                        break;
                    }
                }
            }
            myWorkItems.add(new WorkItem(-1, "全部", R.drawable.all));
        }
        callBack.showAllWorkItem(allWorkItems);
        callBack.showWorkItem(myWorkItems);
    }
}
