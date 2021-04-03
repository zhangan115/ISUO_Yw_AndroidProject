package com.isuo.yw2application.mode.count;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.bean.count.FaultCount;
import com.isuo.yw2application.mode.bean.count.WorkCount;
import com.isuo.yw2application.mode.bean.discover.DeptType;

import rx.Subscription;

/**
 * Created by zhangan on 2017-07-19.
 */

public interface CountDataSource {

    @NonNull
    Subscription getDeptTypeList(@NonNull String type, @NonNull IListCallBack<DeptType> callBack);

    @NonNull
    Subscription getWorkCountData(@NonNull String deptId, @NonNull String time, @NonNull IListCallBack<WorkCount> callBack);

    @NonNull
    Subscription getFaultCountData(@NonNull String deptId, @NonNull String time, @NonNull IListCallBack<FaultCount> callBack);

}
