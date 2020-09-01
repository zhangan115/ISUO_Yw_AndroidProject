package com.isuo.yw2application.mode.inject;

import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.inject.bean.InjectEquipment;
import com.isuo.yw2application.mode.inject.bean.InjectEquipmentLog;
import com.isuo.yw2application.mode.inject.bean.InjectItemBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 注油管理
 * Created by zhangan on 2018/4/12.
 */

public interface InjectOilApi {

    @GET("oil/injection/equipment/list.json")
    Observable<Bean<List<InjectEquipment>>> getInjectionEquipmentList(@QueryMap() Map<String, String> map);

    @GET("oil/injection/page/list.json")
    Observable<Bean<InjectEquipmentLog>> getInjectEquipLogs(@QueryMap() Map<String, String> map);

    @GET("oil/item/page/list.json")
    Observable<Bean<InjectItemBean>> getInjectItem();

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("oil/injection/add.json")
    Observable<Bean<String>> injectOil(@Body() String info);
}
