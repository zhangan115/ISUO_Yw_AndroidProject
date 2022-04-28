package com.isuo.yw2application.mode.create;

import com.isuo.yw2application.mode.Bean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创建对象
 * Created by zhangan on 2018/3/30.
 */

public interface CreateApi {


    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @GET("room/add.json")
    Observable<Bean<String>> addRoom(@Query("roomType") int type, @Query("roomName") String roomName);


    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @GET("equipment/type/add.json")
    Observable<Bean<String>> addEquipmentType(@Query("parentId") Long parentId,@Query("level") int level,@Query("equipmentTypeName") String roomName);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("equipment/app/add.json")
    Observable<Bean<String>> addEquipment(@Body() String jsonInfo);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("equipment/app/edit.json")
    Observable<Bean<String>> editEquipment(@Body() String jsonInfo);

    /**
     * 删除对象类型
     *
     * @param equipmentTypeId 对象类型id
     * @return 订阅
     */
    @GET("equipment/type/delete.json")
    Observable<Bean<String>> deleteEquipmentType(@Query("equipmentTypeId") long equipmentTypeId);

    /**
     * 删除属地
     *
     * @param roomId 属地id
     * @return 订阅
     */
    @GET("room/delete.json")
    Observable<Bean<String>> deleteRoom(@Query("roomId") long roomId);

}


