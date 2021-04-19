package com.isuo.yw2application.api;

import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.fire.FireCountBean;
import com.isuo.yw2application.mode.fire.FireListBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface FireApi {

    /**
     * 统计消防首页上的几个数字
     *
     * @return 订阅
     */
    @GET("fire/equipment/count.json")
    Observable<Bean<FireCountBean>> getFireEquipmentCount(@QueryMap() Map<String, String> map);

    /**
     * 获取消防设备区域列表
     * @return 订阅
     */
    @GET("fire/equipment/app/room/list.json")
    Observable<Bean<List<String>>> getFireEquipmentRoomList(@QueryMap() Map<String, String> map);

    /**
     * 获取消防设备列表
     * @return 订阅
     */
    @GET("fire/equipment/app/list.json")
    Observable<Bean<List<FireListBean>>> getFireEquipmentList(@QueryMap() Map<String, String> map);

    /**
     * 统计消防状态
     *
     * @return 订阅
     */
    @GET("fire/equipment/state/count.json")
    Observable<Bean<FireCountBean>> getFireStateCount(@QueryMap() Map<String, String> map);



}
