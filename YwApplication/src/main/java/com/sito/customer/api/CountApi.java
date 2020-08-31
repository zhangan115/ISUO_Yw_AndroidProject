package com.sito.customer.api;

import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.count.FaultCount;
import com.sito.customer.mode.bean.count.WorkCount;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 统计API
 * Created by zhangan on 2017-07-19.
 */

public interface CountApi {

    /**
     * 工作量统计，根据时间（年月）和（部门）
     *
     * @param deptId 部门
     * @param time   年月
     * @return 订阅
     */
    @GET("statistics/user/workload.json")
    Observable<Bean<List<WorkCount>>> getWorkCount(@Query("deptId") String deptId, @Query("time") String time);

    /**
     * 故障处理统计，根据时间（年月）和（部门）
     *
     * @param deptId 部门
     * @param time   年月
     * @return 订阅
     */
    @GET("statistics/user/fault/handle.json")
    Observable<Bean<List<FaultCount>>> getFaultCount(@Query("deptId") String deptId, @Query("time") String time);
}
