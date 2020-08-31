package com.sito.customer.mode.tools;

import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.sos.EmergencyCall;
import com.sito.customer.mode.tools.bean.CheckListBean;
import com.sito.customer.mode.tools.bean.Tools;
import com.sito.customer.mode.tools.bean.ToolsLog;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 工具api
 * Created by zhangan on 2018/4/2.
 */

public interface ToolsApi {
    /**
     * 查询工具列表
     */
    @GET("tools/list.json")
    Observable<Bean<List<Tools>>> getToolList(@QueryMap() Map<String, String> map);

    /**
     * 查询工具详情
     */
    @GET("tools/tools.json")
    Observable<Bean<Tools>> getToolsTools(@Query("toolId") long toolId);

    /**
     * 借用详情
     */
    @GET("tools/toolLog.json")
    Observable<Bean<List<EmergencyCall>>> getToolsToolLog(@Query("toolId") long toolId);

    /**
     * 归还时，查询借用信息
     */
    @GET("tools/getToolsLog.json")
    Observable<Bean<ToolsLog>> getToolsLog(@Query("toolsId") long toolId);

    /**
     * 外借
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("tools/toolOut.json")
    Observable<Bean<String>> getToolsToolOut(@Body() String json);

    /**
     * 新增/修改工具
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("tools/add.json")
    Observable<Bean<String>> addTools(@Body() String json);


    /**
     * 借用人确认借用工具
     */
    @GET("tools/userConfirm.json")
    Observable<Bean<String>> toolsUserConfirm(@Query("logId") long logId, @Query("isConfirm") int state);

    /**
     * 获取check list
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("tools/checkList.json")
    Observable<Bean<List<CheckListBean>>> getCheckList(@Body() String json);

    /**
     * 归还借用工具
     */
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("tools/returnTool.json")
    Observable<Bean<String>> userReturnTool(@Body() String json);

    @GET("tools/sendMessage.json")
    Observable<Bean<String>> askReturnTools(@Query("logId") Long logId);

    @GET("tools/toolPreOut.json")
    Observable<Bean<String>> getToolsState(@Query("toolId") Long logId);


}
