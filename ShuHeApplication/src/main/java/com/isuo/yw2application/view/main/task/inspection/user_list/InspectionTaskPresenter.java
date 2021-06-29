package com.isuo.yw2application.view.main.task.inspection.user_list;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.work.InspectionBean;
import com.isuo.yw2application.mode.work.WorkRepository;
import com.sito.library.utils.DataUtil;
import com.umeng.commonsdk.debug.I;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class InspectionTaskPresenter implements InspectionTaskContract.Presenter {

    private final WorkRepository mRepository;
    private final InspectionTaskContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    InspectionTaskPresenter(WorkRepository mRepository, InspectionTaskContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getTaskList(int userId, int count) {
        JSONObject jsonObject = new JSONObject();
        String startTime;
        String endTime;
        Calendar calendar = Calendar.getInstance();
        endTime = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH, -40);
        startTime = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
        try {
            jsonObject.put("startTimeA", startTime);
            jsonObject.put("endTimeA", endTime);
            jsonObject.put("taskState", 4);
            jsonObject.put("userId", userId);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mView.showLoading();
        mSubscriptions.add(mRepository.getInspectionData(jsonObject, new IListCallBack<InspectionBean>() {
            @Override
            public void onSuccess(@NonNull List<InspectionBean> list) {
                mView.showData(list);
            }

            @Override
            public void onError(String message) {
                mView.noData();
            }

            @Override
            public void onFinish() {
                mView.hideLoading();
            }
        }));
    }

    @Override
    public void getMoreTaskList(int userId, int count, final long lastId) {
        JSONObject jsonObject = new JSONObject();
        String startTime;
        String endTime;
        Calendar calendar = Calendar.getInstance();
        endTime = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH, -40);
        startTime = DataUtil.timeFormat(calendar.getTimeInMillis(), "yyyy-MM-dd");
        try {
            jsonObject.put("lastId", lastId);
            jsonObject.put("startTimeA", startTime);
            jsonObject.put("endTimeA", endTime);
            jsonObject.put("taskState", 4);
            jsonObject.put("userId", userId);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscriptions.add(mRepository.getInspectionData(jsonObject, new IListCallBack<InspectionBean>() {
            @Override
            public void onSuccess(@NonNull List<InspectionBean> list) {
                mView.showMoreData(list);
            }

            @Override
            public void onError(String message) {
                mView.noMoreData();
            }

            @Override
            public void onFinish() {
                mView.hideLoadingMore();
            }
        }));
    }
}
