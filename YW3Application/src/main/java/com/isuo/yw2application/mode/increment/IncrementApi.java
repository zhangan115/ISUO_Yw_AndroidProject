package com.isuo.yw2application.mode.increment;

import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.UploadResult;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 专项工作
 * Created by zhangan on 2018/4/3.
 */

public interface IncrementApi {

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("increment/end.json")
    Observable<Bean<String>> finishIncrement(@Body String body);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("increment/add.json")
    Observable<Bean<UploadResult>> upLoadIncrement(@Body String body);

    @GET("increment/start.json")
    Observable<Bean<String>> startIncrement(@Query("workId") Long workId);

}
