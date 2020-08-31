package com.sito.customer.mode.inspection;

import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.equip.FocusBean;
import com.sito.customer.mode.bean.inspection.InspectionDetailBean;
import com.sito.customer.mode.bean.inspection.SecureBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 巡检api
 * Created by zhangan on 2018/3/20.
 */

public interface InspectionApi {

    @GET("bag/security/get.json")
    Observable<Bean<SecureBean>> getSecureInfo(@Query("securityId") long securityId);

    /**
     * 获取巡检详情列表
     *
     * @param taskId 巡检列表
     * @return 请求对象
     */
    @GET("task/get/task.json")
    Observable<Bean<InspectionDetailBean>> getInspectionDetailList(@Query("taskId") long taskId);

    //开始 结束
    @GET("task/edit/roomstate.json")
    Observable<Bean<String>> roomStateChange(@Query("taskId") long taskId, @Query("taskRoomId") long taskRoomId, @Query("operation") int operation);

    //列表全部完成
    @GET("task/edit/state.json")
    Observable<Bean<String>> roomListFinish(@Query("taskId") long taskId, @Query("operation") int operation, @Query("userIds") String userIds);


    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("task/upload/inspection.json")
    Observable<Bean<String>> uploadInspection(@Body() String taskInfo);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("task/add/inplace.json")
    Observable<Bean<String>> uploadUserPhoto(@Body() String count);

    @GET("equipment/focus/get.json")
    Observable<Bean<FocusBean>> getCareData(@Query("equipmentId") long equipmentId);
}
