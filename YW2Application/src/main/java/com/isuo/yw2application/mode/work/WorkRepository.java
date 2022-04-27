package com.isuo.yw2application.mode.work;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isuo.yw2application.R;
import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.api.FaultApi;
import com.isuo.yw2application.api.WorkApi;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.PayMenuBean;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.isuo.yw2application.mode.bean.today.TodayToDoBean;
import com.isuo.yw2application.mode.bean.work.AwaitWorkBean;
import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.isuo.yw2application.mode.bean.work.InspectionDataBean;
import com.isuo.yw2application.mode.bean.work.WorkInspectionBean;
import com.isuo.yw2application.mode.bean.work.WorkItem;
import com.isuo.yw2application.mode.bean.work.WorkState;
import com.isuo.yw2application.mode.inspection.InspectionApi;
import com.isuo.yw2application.utils.ACache;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
//            jsonObject.put("flowTime", date);
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
            , @NonNull final IListCallBack<WorkInspectionBean> callBack) {
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
        Observable<Bean<List<WorkInspectionBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getInspection(jsonObject.toString());
        return new ApiCallBack<List<WorkInspectionBean>>(observable) {
            @Override
            public void onSuccess(List<WorkInspectionBean> result) {
                callBack.onFinish();
                if (result == null || result.size() == 0) {
                    callBack.onError("");
                } else {
                    Observable.just(result)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext(inspectionDetailBean -> {
                                ACache.get(Yw2Application.getInstance()).put(data + "_" + inspectionType, new Gson().toJson(result));
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<WorkInspectionBean>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    callBack.onFinish();
                                    callBack.onError(e.getMessage());
                                }

                                @Override
                                public void onNext(List<WorkInspectionBean> inspectionDetailBean) {
                                    callBack.onFinish();
                                    callBack.onSuccess(inspectionDetailBean);
                                }
                            });
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
    public Subscription getInspectionData(JSONObject jsonObject, @NonNull final IListCallBack<WorkInspectionBean> callBack) {
        Observable<Bean<List<WorkInspectionBean>>> observable =
                Api.createRetrofit().create(WorkApi.class).getInspection(jsonObject.toString());
        return new ApiCallBack<List<WorkInspectionBean>>(observable) {
            @Override
            public void onSuccess(List<WorkInspectionBean> been) {
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
    public Subscription get24HFaultList(Map<String, String> map, @NonNull final IListCallBack<FaultList> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latelyTime", 1);
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
        List<WorkItem> allWorkItems = new ArrayList<>();
        List<WorkItem> payWorkItems = new ArrayList<>();
        //可用item
        allWorkItems.add(new WorkItem(2, "执行检修", R.drawable.overhaul));
        allWorkItems.add(new WorkItem(3, "发布检修", R.drawable.assign));
        allWorkItems.add(new WorkItem(4, "发布通知", R.drawable.notice));
        allWorkItems.add(new WorkItem(5, "事件上报", R.drawable.fault_report));
        allWorkItems.add(new WorkItem(6, "台账录入", R.drawable.drive_input));
        allWorkItems.add(new WorkItem(7, "查看规则", R.drawable.standard));
        allWorkItems.add(new WorkItem(8, "待办事项", R.drawable.to_do_list));
        allWorkItems.add(new WorkItem(9, "执行巡检", R.drawable.inspecting));
        //需要付费item
        PayMenuBean payMenuBean = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        if (payMenuBean.getIncrementWorkSo() == 0) {
            payWorkItems.add(new WorkItem(1, "专项工作", R.drawable.special));
        } else {
            allWorkItems.add(new WorkItem(1, "专项工作", R.drawable.special));
        }
        if (payMenuBean.getOilSo() == 0) {
            payWorkItems.add(new WorkItem(20, "润滑管理", R.drawable.oiling));
        } else {
            allWorkItems.add(new WorkItem(20, "润滑管理", R.drawable.oiling));
        }
        if (payMenuBean.getToolSo() == 0) {
            payWorkItems.add(new WorkItem(21, "工具管理", R.drawable.tool_mgt));
        } else {
            allWorkItems.add(new WorkItem(21, "工具管理", R.drawable.tool_mgt));
        }

        String workItemStr = sp.getString(ConstantStr.WORK_ITEM, "");
        String[] items = workItemStr.split(",");

        List<WorkItem> myWorkItems = new ArrayList<>();

        for (String item : items) {
            if (TextUtils.isEmpty(item)) {
                break;
            }
            int id = Integer.parseInt(item);
            for (WorkItem allItem : allWorkItems) {
                if (allItem.getId() == id) {
                    myWorkItems.add(allItem);
                    break;
                }
            }
            for (WorkItem payItem : payWorkItems) {
                if (payItem.getId() == id) {
                    myWorkItems.add(payItem);
                    break;
                }
            }
        }
        if (myWorkItems.isEmpty()) {
            myWorkItems.add(new WorkItem(8, "待办事项", R.drawable.to_do_list));
            myWorkItems.add(new WorkItem(2, "执行检修", R.drawable.overhaul));
            myWorkItems.add(new WorkItem(3, "发布检修", R.drawable.assign));
            myWorkItems.add(new WorkItem(4, "发布通知", R.drawable.notice));
            myWorkItems.add(new WorkItem(5, "事件上报", R.drawable.fault_report));
            myWorkItems.add(new WorkItem(6, "台账录入", R.drawable.drive_input));
            myWorkItems.add(new WorkItem(7, "查看规则", R.drawable.standard));
        }
        myWorkItems.add(new WorkItem(-1, "全部", R.drawable.all));
        callBack.showWorkItem(myWorkItems);
    }

    @Override
    public void getAllWorkItems(WorkItemAllCallBack callBack) {
        this.getWorkItems(callBack);
        List<WorkItem> allWorkItems = new ArrayList<>();
        List<WorkItem> payWorkItems = new ArrayList<>();
        //可用item
        allWorkItems.add(new WorkItem(2, "执行检修", R.drawable.overhaul));
        allWorkItems.add(new WorkItem(3, "发布检修", R.drawable.assign));
        allWorkItems.add(new WorkItem(4, "发布通知", R.drawable.notice));
        allWorkItems.add(new WorkItem(5, "事件上报", R.drawable.fault_report));
        allWorkItems.add(new WorkItem(6, "台账录入", R.drawable.drive_input));
        allWorkItems.add(new WorkItem(7, "查看规则", R.drawable.standard));
        allWorkItems.add(new WorkItem(8, "待办事项", R.drawable.to_do_list));
        allWorkItems.add(new WorkItem(9, "执行巡检", R.drawable.inspecting));
        //需要付费item
        PayMenuBean payMenuBean = Yw2Application.getInstance().getCurrentUser().getCustomerSetMenu();
        if (payMenuBean.getIncrementWorkSo() == 0) {
            payWorkItems.add(new WorkItem(1, "专项工作", R.drawable.special));
        } else {
            allWorkItems.add(new WorkItem(1, "专项工作", R.drawable.special));
        }
        if (payMenuBean.getOilSo() == 0) {
            payWorkItems.add(new WorkItem(20, "润滑管理", R.drawable.oiling));
        } else {
            allWorkItems.add(new WorkItem(20, "润滑管理", R.drawable.oiling));
        }
        if (payMenuBean.getToolSo() == 0) {
            payWorkItems.add(new WorkItem(21, "工具管理", R.drawable.tool_mgt));
        } else {
            allWorkItems.add(new WorkItem(21, "工具管理", R.drawable.tool_mgt));
        }
        callBack.showAllWorkItem(allWorkItems);
        callBack.showPayWorkItem(payWorkItems);
    }

    @Override
    public void saveWorkItems(List<WorkItem> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == -1) {
                continue;
            }
            sb.append(items.get(i).getId());
            if (i != items.size() - 1) {
                sb.append(",");
            }
        }
        sp.edit().putString(ConstantStr.WORK_ITEM, sb.toString()).apply();
    }
}
