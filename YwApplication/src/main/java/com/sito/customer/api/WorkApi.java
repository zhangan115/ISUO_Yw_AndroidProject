package com.sito.customer.api;

import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.equip.EquipType;
import com.sito.customer.mode.bean.work.AwaitWorkBean;
import com.sito.customer.mode.bean.work.IncrementBean;
import com.sito.customer.mode.bean.work.InspectionBean;
import com.sito.customer.mode.bean.work.InspectionDataBean;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 工作接口
 * Created by zhangan on 2017-07-14.
 */

public interface WorkApi {

    /**
     * 获取代办任务
     * <br>
     * flowTime 筛选的时间
     * count 每页多少条，app和bs使用
     * lastId 上一次查询的最后一个ID，app分页用
     * </br>
     *
     * @param info 参数
     * @return 订阅
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("fault/listForUser.json")
    Observable<Bean<List<AwaitWorkBean>>> getAwaitWork(@Body() String info);

    /**
     * 获取增值工作
     * <br>
     * agentType 0为巡检，1为客户，不传为pc
     * time 筛选的时间
     * count 每页多少条，app和bs使用
     * lastId 上一次查询的最后一个ID，app分页用
     * </br>
     *
     * @param info 参数
     * @return 订阅
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("increment/page/list.json")
    Observable<Bean<List<IncrementBean>>> getIncrement(@Body() String info);

    /**
     * 获取检修工作
     * <br>
     * time 筛选的时间
     * count 每页多少条，app和bs使用
     * lastId 上一次查询的最后一个ID，app分页用
     * </br>
     *
     * @param info 参数
     * @return 订阅
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("repair/listForUser.json")
    Observable<Bean<List<OverhaulBean>>> getOverhaul(@Body() String info);

    /**
     * 获取巡检列表
     * <br>
     * agentType 是巡检版还是客户版，0为巡检，1为客户，不传为pc
     * time 日期
     * count 每页多少条，app和bs使用
     * lastId 上一次查询的最后一个ID，app分页用
     * </br>
     *
     * @param info 参数
     * @return 订阅
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("task/list.json")
    Observable<Bean<List<InspectionBean>>> getInspection(@Body() String info);


    /**
     * 领取任务
     *
     * @param taskId        任务id
     * @param operationType 类型
     * @return 请求对象
     */
    @GET("task/edit/state.json")
    Observable<Bean<String>> operationTask(@Query("taskId") String taskId, @Query("operation") int operationType);

    /**
     * 获取检修详情
     *
     * @param repairId 故障id
     * @return 订阅
     */
    @GET("repair/get.json")
    Observable<Bean<OverhaulBean>> getRepairDetail(@Query("repairId") String repairId);

    /**
     * 设备类型
     *
     * @return 订阅
     */
    @GET("equipment/type/list.json")
    Observable<Bean<List<EquipType>>> getEquipmentTypes();

    /**
     * 获取任务的巡检数据
     *
     * @param taskId id
     * @return 订阅
     */
    @GET("task/get/task/data.json")
    Observable<Bean<InspectionDataBean>> getInspectionData(@Query("taskId") Long taskId);

    @GET("increment/get.json")
    Observable<Bean<IncrementBean>> getIncrementData(@Query("workId") Long taskId);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("increment/add.json")
    Observable<Bean<String>> generateIncrement(@Body() String info);

}
