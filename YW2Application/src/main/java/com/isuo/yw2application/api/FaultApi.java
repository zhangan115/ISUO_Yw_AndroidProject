package com.isuo.yw2application.api;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.DefaultFlowBean;
import com.isuo.yw2application.mode.bean.fault.FaultCount;
import com.isuo.yw2application.mode.bean.fault.FaultCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDetail;
import com.isuo.yw2application.mode.bean.fault.FaultYearCountBean;
import com.isuo.yw2application.mode.bean.fault.JobPackageBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 故障Api
 * Created by zhangan on 2017-07-17.
 */

public interface FaultApi {

    @GET("fault/alarm/count.json")
    Observable<Bean<FaultCountBean>> getFaultCount();

    @GET("fault/day/count.json")
    Observable<Bean<FaultDayCountBean>> getFaultDayCount(@Query("dayTime") String time);

    @GET("fault/count/year.json")
    Observable<Bean<FaultYearCountBean>> getFaultYearCount(@Query("yearTime") String time);

    @GET("fault/roomEquipmentList.json")
    Observable<Bean<FaultCountBean>> getFaultRoomEquipment();

    @GET("statistics/relic/fault.json")
    Observable<Bean<AlarmCount>> getAlarmCount();

    @GET("fault/homepage/count.json")
    Observable<Bean<FaultCount>> getHomePageFaultCount();


    /**
     * <br>
     * lastId 上一次查询的最后一个ID，app分页用
     * count 每页多少条，app和bs使用
     * byCurrentUser 查询当前用户创建的故障
     * roomId 配电室id
     * equipmentId 设备id
     * taskId 任务id
     * faultState 故障状态
     * faultType 故障等级
     * equipmentType 设备类型
     * startTime 开始时间
     * endTime 结束时间
     * lessThanFaultState 故障状态
     * </br>
     *
     * @param info
     * @return
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("fault/list.json")
    Observable<Bean<List<FaultList>>> getFaultList(@Body() String info);

    /**
     * 故障详情
     *
     * @param faultId 故障id
     * @return 订阅
     */
    @GET("fault/get.json")
    Observable<Bean<FaultDetail>> getFaultDetail(@Query("faultId") String faultId);

    @GET("dept/deptuser/list.json")
    Observable<Bean<List<DepartmentBean>>> getEmployeeList(@Query("deptType") Integer type);

    /**
     * 关闭故障
     *
     * @return 订阅
     */
    @GET("fault/close.json")
    Observable<Bean<String>> getCloseFault(@QueryMap() Map<String, String> map);

    /**
     * 流转分发故障
     *
     * @return 订阅
     */
    @GET("fault/flow.json")
    Observable<Bean<String>> getFlowFault(@QueryMap() Map<String, String> map);

    /**
     * 生成检修任务
     * <br>
     * faultId 故障id
     * startTime 计划开始时间
     * endTime 计划结束时间
     * usersNext 检修员工id
     * repairIntro 检修说明
     * </br>
     *
     * @param info 参数
     * @return 订阅
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("repair/add.json")
    Observable<Bean<String>> repairAdd(@Body() String info);


    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("equipment/focus/add.json")
    Observable<Bean<String>> careEquipment(@Body() String info);

    @GET("bag/job/list/page.json")
    Observable<Bean<JobPackageBean>> getJobPackage(@Query("count") int count);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("fault/add.json")
    Observable<Bean<UploadResult>> upLoadFault(@Body String body);

    @GET("defaultFlow/list.json")
    Observable<Bean<List<DefaultFlowBean>>> getDefaultFlow(@Query("byCurrentUser") int byCurrentUser);

    @POST("base/file/upload.json")
    @Multipart
    Observable<Bean<List<String>>> postVoiceFile(@Part List<MultipartBody.Part> partList);


    @GET("fault/list.json")
    Observable<Bean<List<FaultList>>> getFaultHistoryList(@Query("count") int count
            , @Query("byCurrentUser") @NonNull String byCurrentUser
            , @Query("agentType") String agentType);

    @GET("fault/list.json")
    Observable<Bean<List<FaultList>>> getMoreFaultHistoryList(@Query("lastId") long lastId
            , @Query("count") int count
            , @Query("byCurrentUser") @NonNull String byCurrentUser
            , @Query("agentType") String agentType);
}
