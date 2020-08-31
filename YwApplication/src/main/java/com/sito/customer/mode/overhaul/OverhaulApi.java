package com.sito.customer.mode.overhaul;

import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.bean.overhaul.OverhaulNoteBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 检修api
 * Created by zhangan on 2018/3/20.
 */

public interface OverhaulApi {

    /**
     * 获取检修工作包
     *
     * @return 请求对象
     */
    @GET("bag/job/get.json")
    Observable<Bean<OverhaulNoteBean>> getOverhaulNote(@Query("jobId") long noteId);

    @GET("repair/start.json")
    Observable<Bean<String>> startOverhaul(@Query("repairId") String repairId);

    /**
     * 检修详情
     *
     * @return 请求对象
     */
    @GET("repair/get.json")
    Observable<Bean<OverhaulBean>> getRepairWork(@Query("repairId") String repairId);

    /**
     * 提交检修任务
     *
     * @return 请求对象
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("repair/endRepair.json")
    Observable<Bean<String>> uploadRepairWork(@Body() String repairContent);

}
