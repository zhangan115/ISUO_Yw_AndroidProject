package com.sito.customer.mode.count;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.customer.api.Api;
import com.sito.customer.api.ApiCallBack;
import com.sito.customer.api.CountApi;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.bean.count.FaultCount;
import com.sito.customer.mode.bean.count.WorkCount;
import com.sito.customer.mode.bean.discover.DeptType;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscription;

/**
 * 统计实现
 * Created by zhangan on 2017-07-19.
 */
@Singleton
public class CountRepository implements CountDataSource {


    private SharedPreferences sp;

    @Inject
    public CountRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Subscription getDeptTypeList(@NonNull String type, @NonNull final IListCallBack<DeptType> callBack) {
        Observable<Bean<List<DeptType>>> observable = Api.createRetrofit().create(Api.Count.class).getDeptType(type);
        return new ApiCallBack<List<DeptType>>(observable) {
            @Override
            public void onSuccess(@Nullable List<DeptType> deptTypes) {
                callBack.onFinish();
                if (deptTypes == null || deptTypes.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(deptTypes);
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
    public Subscription getWorkCountData(@NonNull String deptId, @NonNull String time, @NonNull final IListCallBack<WorkCount> callBack) {
        Observable<Bean<List<WorkCount>>> observable = Api.createRetrofit().create(CountApi.class).getWorkCount(deptId, time);
        return new ApiCallBack<List<WorkCount>>(observable) {
            @Override
            public void onSuccess(@Nullable List<WorkCount> workCounts) {
                callBack.onFinish();
                if (workCounts == null || workCounts.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(workCounts);
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
    public Subscription getFaultCountData(@NonNull String deptId, @NonNull String time, @NonNull final IListCallBack<FaultCount> callBack) {
        Observable<Bean<List<FaultCount>>> observable = Api.createRetrofit().create(CountApi.class).getFaultCount(deptId, time);
        return new ApiCallBack<List<FaultCount>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultCount> faultCounts) {
                callBack.onFinish();
                if (faultCounts == null || faultCounts.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(faultCounts);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }


}
