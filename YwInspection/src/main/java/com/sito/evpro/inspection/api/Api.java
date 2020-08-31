package com.sito.evpro.inspection.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sito.evpro.inspection.BuildConfig;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.bean.EmergencyCall;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.employee.DepartmentBean;
import com.sito.evpro.inspection.mode.bean.equip.CheckValue;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.equip.EquipRecordDetail;
import com.sito.evpro.inspection.mode.bean.equip.EquipRoom;
import com.sito.evpro.inspection.mode.bean.equip.EquipType;
import com.sito.evpro.inspection.mode.bean.equip.InspectionData;
import com.sito.evpro.inspection.mode.bean.equip.TimeLineBean;
import com.sito.evpro.inspection.mode.bean.fault.CheckBean;
import com.sito.evpro.inspection.mode.bean.fault.DefaultFlowBean;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectResultBean;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDataBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.OperationBean;
import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;
import com.sito.library.utils.SPHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * api
 * Created by zhangan on 2017-06-21.
 */

public class Api {
    //测试服务器
//    public static final String HOST = "http://172.16.40.240:8888/sitopeuv/api";
//    public static final String HOST = "http://172.16.40.250:8088/sitopeuv/api";
    public static final String HOST = BuildConfig.HOST;
//    public static final String HOST = "https://www.ewaypro.cn/euvtest/api";
//    public static final String HOST = "http://172.16.40.26:8081/sitopeuv/api";
//    public static final String HOST = "http://172.16.70.109:8888/sitopeuv/api";
//    public static final String HOST = "http://172.16.40.249:8080/sitopeuv/api";

    private static Retrofit mRetrofit;
    private static final int CONNECT_TIME = 5;
    private static final int READ_TIME = 20;
    private static final int WRITE_TIME = 20;
    private static Cookie cookie;

    public static Retrofit createRetrofit() {
        if (mRetrofit == null) {
            initRetrofit();
        }
        return mRetrofit;
    }

    private static void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        builder.cookieJar(new CookieJar() {

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    cookie = cookies.get(0);
                    SPHelper.write(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN, cookie.domain());
                    SPHelper.write(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME, cookie.name());
                    SPHelper.write(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE, cookie.value());
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = new ArrayList<>();
                if (cookie == null) {
                    String doMin = SPHelper.readString(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN);
                    String name = SPHelper.readString(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME);
                    String value = SPHelper.readString(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE);
                    if (!TextUtils.isEmpty(doMin) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                        cookie = new Cookie.Builder().domain(doMin).name(name).value(value).build();
                    }
                }
                if (cookie != null) {
                    cookies.add(new Cookie.Builder().domain(cookie.domain()).name(cookie.name()).value(cookie.value()).build());
                }
                return cookies;
            }
        });
        String host = InspectionApp.getInstance().AppHost();
        if (!host.endsWith("/")) {
            host = host + "/";
        }
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(host)
                .addConverterFactory(ProtoConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public static void clean() {
        cookie = null;
        mRetrofit = null;
        SPHelper.remove(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN);
        SPHelper.remove(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME);
        SPHelper.remove(InspectionApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE);
    }

    public static Cookie getCookie() {
        return cookie;
    }

    public interface Login {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/login.json")
        Observable<Bean<User>> userLogin(@Body() String info);

    }

    public interface Inspection {

        /**
         * 获取用户下巡检任务列表
         *
         * @param type     客户版
         * @param time     时间
         * @param pageSize 分页
         * @return 请求对象
         */
        @GET("task/list.json")
        Observable<Bean<List<InspectionBean>>> getInspectionList(@Query("agentType") int type, @Query("time") String time, @Query("count") int pageSize);

        /**
         * * 获取用户下巡检任务列表
         *
         * @param type     客户版
         * @param time     时间
         * @param pageSize 分页
         * @param lastId   上一次查询的最后一个ID
         * @return 请求对象
         */
        @GET("task/list.json")
        Observable<Bean<List<InspectionBean>>> getInspectionList(@Query("agentType") int type, @Query("time") String time, @Query("count") int pageSize, @Query("lastId") String lastId);

        /**
         * 领取任务
         *
         * @param taskId        任务id
         * @param operationType 类型
         * @return 请求对象
         */
        @GET("task/edit/state.json")
        Observable<Bean<OperationBean>> operationTask(@Query("taskId") String taskId, @Query("operation") int operationType);

        /**
         * 用户待办检修
         *
         * @return 请求对象
         */
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("repair/listForUser.json")
        Observable<Bean<List<OverhaulBean>>> getOverhaulList(@Body() String info);


        /**
         * 获取检修列表
         *
         * @param time     时间
         * @param pageSize 分页大小
         * @return 请求对象
         */
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @GET("increment/page/list.json")
        Observable<Bean<List<IncrementBean>>> getIncrementList(@Query("agentType") int agentType, @Query("queryTime") String time, @Query("count") int pageSize);

        /**
         * 获取检修列表
         *
         * @param time     时间
         * @param pageSize 分页大小
         * @return 请求对象
         */
        @GET("increment/page/list.json")
        Observable<Bean<List<IncrementBean>>> getIncrementList(@Query("time") String time, @Query("count") int pageSize, @Query("lastId") String lastId);

        /**
         * 获取巡检详情列表
         *
         * @param taskId 巡检列表
         * @return 请求对象
         */
        @GET("task/get/task.json")
        Observable<Bean<InspectionDetailBean>> getInspectionDetailList(@Query("taskId") String taskId);

        /**
         * 检修详情
         *
         * @return 请求对象
         */
        @GET("repair/get.json")
        Observable<Bean<OverhaulBean>> getRepairWork(@Query("repairId") String repairId);

        /**
         * 提交检修任务
         *
         * @return 请求对象
         */
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("repair/endRepair.json")
        Observable<Bean<String>> uploadRepairWork(@Body() String repairContent);

        /**
         * 检修详情
         *
         * @return 请求对象
         */
        @GET("dept/deptuser/list.json")
        Observable<Bean<List<DepartmentBean>>> getEmployeeList();

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("task/add/inplace.json")
        Observable<Bean<String>> uploadUserPhoto(@Body() String count);

        /**
         * 获取检修详情
         *
         * @param repairId 故障id
         * @return 订阅
         */
        @GET("repair/get.json")
        Observable<Bean<OverhaulBean>> getRepairDetail(@Query("repairId") String repairId);


        /**
         * 获取检修工作包
         *
         * @return 请求对象
         */
        @GET("bag/job/get.json")
        Observable<Bean<OverhaulNoteBean>> getOverhaulNote(@Query("jobId") String noteId);

        /**
         * 获取任务的巡检数据
         *
         * @param taskId id
         * @return 订阅
         */
        @GET("task/get/task/data.json")
        Observable<Bean<InspectionDataBean>> getInspectionData(@Query("taskId") Long taskId);

        @GET("increment/start.json")
        Observable<Bean<String>> startIncrement(@Query("workId") Long workId);

        @GET("repair/start.json")
        Observable<Bean<String>> startOverhaul(@Query("repairId") Long repairId);

    }

    public interface Increment {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("increment/add.json")
        Observable<Bean<UploadResult>> upLoadIncrement(@Body String body);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("increment/end.json")
        Observable<Bean<String>> finishIncrement(@Body String body);

        @GET("increment/start.json")
        Observable<Bean<String>> startIncrement(@Query("workId") long workId);
    }

    public interface Fault {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("fault/add.json")
        Observable<Bean<UploadResult>> upLoadFault(@Body String body);

        @GET("defaultFlow/list.json")
        Observable<Bean<List<DefaultFlowBean>>> getDefaultFlow(@Query("byCurrentUser") int byCurrentUser);
    }

    //获取字典
    public interface Option {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("base/option/item.json")
        Observable<Bean<List<OptionBean>>> getOptionInfo();
    }

    //获取设备
    public interface Equip {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("room/equipment/list.json")
        Observable<Bean<List<EquipBean>>> getEquipInfo();

        //设备类型列表 不分页
        @GET("equipment/type/list.json")
        Observable<Bean<List<EquipType>>> getEquipType();

        //设备详情
        @GET("equipment/get.json")
        Observable<Bean<EquipmentBean>> getEquipmentDetail(@Query("equipmentId") long taskId);

        //获取配电室列表
        @GET("room/user/list.json")
        Observable<Bean<List<EquipRoom>>> getEquipPlace();

        //查询设备
        @GET("equipment/list.json")
        Observable<Bean<List<EquipmentBean>>> getEquipList(@QueryMap Map<String, Object> map);

        //根据taskId查询设备
        @GET("task/get/room/equipments.json")
        Observable<Bean<List<EquipBean>>> getEquipByTaskId(@Query("taskId") long taskId);


        //根据设备id获取检修记录
        @GET("repair/equipment/list.json")
        Observable<Bean<List<OverhaulBean>>> getOverByEId(@Query("equipmentId") long equipId, @Query("count") int pageSize);

        @GET("repair/list.json")
        Observable<Bean<List<OverhaulBean>>> getMoreOverByEId(@Query("equipmentId") long equipId, @Query("count") int pageSize, @Query("lastId") int lastId);

        //根据设备id获取检修数据
        @GET("inspectionData/item/list.json")
        Observable<Bean<InspectionData>> getCheckData(@Query("equipmentId") long equipId);

        @GET("inspectionData/item/value/list.json")
        Observable<Bean<CheckValue>> getCheckValue(@Query("equipmentId") long equipId, @Query("inspectionId") long inspectionId);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultByEId(@Query("equipmentId") long equipId, @Query("count") int pageSize);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getMoreFaultByEId(@Query("count") int pageSize, @Query("equipmentId") long equipmentId, @Query("lastId") int lastId);


        @GET("equipment/record/list.json")
        Observable<Bean<List<TimeLineBean>>> getTimeLineData(@Query("count") int pageSize, @Query("equipmentId") long equipmentId, @Query("type") int type);


        @GET("equipment/record/get.json")
        Observable<Bean<EquipRecordDetail>> getRecordData(@Query("equipmentRecordId") long equipmentId);

        @Streaming
        @GET
        Call<ResponseBody> downloadFile(@Url String fileUrl);
    }

    //提交文件
    public interface File {

        @POST("base/file/upload.json")
        @Multipart
        Observable<Bean<List<String>>> postImageFile(@Part List<MultipartBody.Part> partList);

        @POST("base/file/upload.json")
        @Multipart
        Observable<Bean<List<String>>> postVoiceFile(@Part List<MultipartBody.Part> partList);
    }

    //历史故障
    public interface HistoryFault {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultList(@Query("count") int count, @Query("byCurrentUser") @NonNull String byCurrentUser, @Query("agentType") String agentType);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getMoreFaultList(@Query("lastId") long lastId, @Query("count") int count, @Query("byCurrentUser") @NonNull String byCurrentUser, @Query("agentType") String agentType);
    }

    //增值工作历史记录
    public interface HistoryIncre {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @GET("increment/page/list.json")
        Observable<Bean<List<IncrementBean>>> getIncreList(@Query("count") int count, @Query("agentType") String agentType);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @GET("increment/page/list.json")
        Observable<Bean<List<IncrementBean>>> getMoreIncreList(@Query("lastId") long lastId, @Query("count") int count, @Query("agentType") String agentType);
    }

    public interface UploadInspection {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("task/upload/inspection.json")
        Observable<Bean<String>> uploadInspection(@Body() String taskInfo);
    }

    public interface TaskInfo {
        //开始 结束
        @GET("task/edit/roomstate.json")
        Observable<Bean<String>> uploadStartOrEnd(@Query("taskId") long taskId, @Query("taskRoomId") long taskRoomId, @Query("operation") int operation);

        //列表全部完成
        @GET("task/edit/state.json")
        Observable<Bean<String>> uploadTaskAll(@Query("taskId") long taskId, @Query("operation") int operation, @Query("userIds") String userIds);

        //巡检详情头部
        @GET("task/taskinfo.json")
        Observable<Bean<CheckBean>> getCheckInfo(@Query("taskId") long taskId);

        //故障列表
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultList(@Query("taskId") long taskId, @Query("count") int pageSize);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getMoreFaultList(@Query("count") int pageSize, @Query("taskId") long taskId, @Query("lastId") int lastId);

        @GET("bag/security/get.json")
        Observable<Bean<SecureBean>> getSecureInfo(@Query("securityId") long securityId);
    }

    public interface NewVersionApi {

        @GET("version/latestInspectionApp.json")
        Observable<Bean<NewVersion>> newVersion();
    }

    public interface UserPhoto {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/appEdit.json")
        Observable<Bean<NewVersion>> updateUser(@Body() String info);
    }

    public interface UserExitApp {

        @GET("user/logout.json")
        Observable<Bean<String>> exitApp();
    }

    public interface Suggest {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("feedback/add.json")
        Observable<Bean<String>> postSuggest(@Body() String info);
    }


    public interface UserUpdate {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/appEdit.json")
        Observable<Bean<String>> updateUserInfo(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/resetPwd.json")
        Observable<Bean<String>> updateUserPassWord(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/sendCode.json")
        Observable<Bean<String>> sendCode(@Body() String info);
    }

    public interface GetEmergency {

        @GET("emergency/listAll.json")
        Observable<Bean<List<EmergencyCall>>> getEmergencyCalls();

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("message/send/urgent")
        Observable<Bean<String>> uploadEmergencyData(@Body() String info);
    }

    public interface InjectEquipmentApi {

        @GET("room/user/list.json")
        Observable<Bean<List<InjectRoomBean>>> getInjectRoom(@Query("roomType") int roomType);

        @GET("room/get/equipments.json")
        Observable<Bean<List<InjectEquipment>>> getInjectEquipment(@Query("roomId") long roomId);

        @GET("oil//injection.json")
        Observable<Bean<InjectResultBean>> injectEquipment(@Query("equipmentId") long equipmentId, @Query("cycle") int cycle);
    }

}
