package com.isuo.yw2application.mode.generate;

import com.isuo.yw2application.mode.Bean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 生成检修任务,专项任务的api
 * Created by zhangan on 2017/9/29.
 */

public interface GenerateApi {

    @POST("base/file/upload.json")
    @Multipart
    Observable<Bean<List<String>>> postFile(@Part List<MultipartBody.Part> partList);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("increment/add.json")
    Observable<Bean<String>> addIncrement(@Body String body);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("repair/noFault/add.json")
    Observable<Bean<String>> addRepair(@Body String body);

}
