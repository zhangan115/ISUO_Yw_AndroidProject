package com.isuo.yw2application.mode.sos;

import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.bean.sos.EmergencyCall;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * sos接口
 * Created by zhangan on 2018/3/29.
 */

public interface SOSApi {

    @GET("emergency/listAll.json")
    Observable<Bean<List<EmergencyCall>>> getEmergencyCalls();

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("message/send/urgent")
    Observable<Bean<String>> uploadEmergencyData(@Body() String info);
}
