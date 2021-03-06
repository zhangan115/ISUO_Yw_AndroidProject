package com.sito.customer.api;

import android.text.TextUtils;

import com.sito.customer.BuildConfig;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.check.CheckBean;
import com.sito.customer.mode.bean.check.CheckValue;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.bean.check.InspectionData;
import com.sito.customer.mode.bean.count.ComeCount;
import com.sito.customer.mode.bean.count.MonthCount;
import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.customer.mode.bean.count.WeekCount;
import com.sito.customer.mode.bean.count.WeekList;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.bean.discover.FaultLevel;
import com.sito.customer.mode.bean.discover.FaultReport;
import com.sito.customer.mode.bean.discover.StandBean;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.mode.bean.equip.EquipBean;
import com.sito.customer.mode.bean.equip.EquipRecordDetail;
import com.sito.customer.mode.bean.equip.EquipRoom;
import com.sito.customer.mode.bean.equip.EquipType;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.bean.equip.TimeLineBean;
import com.sito.customer.mode.bean.news.EnterpriseDetailBean;
import com.sito.customer.mode.bean.news.MessageListBean;
import com.sito.customer.mode.bean.today.TodayToDoBean;
import com.sito.customer.mode.bean.work.WorkState;
import com.sito.customer.mode.inject.bean.InjectEquipment;
import com.sito.customer.mode.inject.bean.InjectRoomBean;
import com.sito.customer.mode.bean.option.OptionBean;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.bean.work.WorkCount;
import com.sito.library.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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
    public static final String HOST = BuildConfig.HOST;
//    public static final String HOST = "http:172.16.40.240:8080/sitopeuv/api";
//    public static final String HOST = "http:172.16.40.249:8080/sitopeuv/api";
//    public static final String HOST = "http://172.16.40.36:8888/sitopeuv/api";
//    public static final String HOST = "http://172.16.40.26:8081/sitopeuv/api";

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
                    SPHelper.write(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN, cookie.domain());
                    SPHelper.write(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME, cookie.name());
                    SPHelper.write(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE, cookie.value());
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = new ArrayList<>();
                if (cookie == null) {
                    String doMin = SPHelper.readString(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN);
                    String name = SPHelper.readString(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME);
                    String value = SPHelper.readString(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE);
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
        String host = CustomerApp.getInstance().AppHost();
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
        SPHelper.remove(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_DOMAIN);
        SPHelper.remove(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_NAME);
        SPHelper.remove(CustomerApp.getInstance().getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.COOKIE_VALUE);
    }


    public interface Login {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/login.json")
        Observable<Bean<User>> userLogin(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("base/option/item.json")
        Observable<Bean<List<OptionBean>>> getOptionInfo();

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("application/record/add.json")
        Observable<Bean<String>> userRegister(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("application/record/sendCode.json")
        Observable<Bean<String>> getRegisterCode(@Body() String info);

    }

    public interface MessageApi {

        @GET("message/get/list.json")
        Observable<Bean<List<MessageListBean>>> getMessageList(@QueryMap() Map<String, String> map);

        @GET("message/read.json")
        Observable<Bean<EnterpriseDetailBean>> getMessageDetail(@Query("messageId") long id);

        @GET("message/readMessage.json")
        Observable<Bean<MessageListBean>> getFireSaveMessage(@Query("messageId") long id);
    }

    //获取设备
    public interface Equip {
        //获取设备 带配电室
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("room/equipment/list.json")
        Observable<Bean<List<EquipBean>>> getEquipInfo(@Body() String body);

        //设备类型列表 不分页
        @GET("equipment/type/list.json")
        Observable<Bean<List<EquipType>>> getEquipType();

        //获取配电室列表
        @GET("room/user/list.json")
        Observable<Bean<List<EquipRoom>>> getEquipPlace();

        //查询设备
        @GET("equipment/list.json")
        Observable<Bean<List<EquipmentBean>>> getEquipList(@QueryMap Map<String, Object> map);

        //设备详情
        @GET("equipment/get.json")
        Observable<Bean<EquipmentBean>> getEquipmentDetail(@Query("equipmentId") long taskId);

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

    //统计
    public interface Count {
        //人员今日昨日到岗统计
        @GET("task/unstation/situation.json")
        Observable<Bean<List<ComeCount>>> getTodaCount(@Query("time") String time, @Query("deptId") String deptId);

        //本周未到岗统计
        @GET("task/unstation/week.json")
        Observable<Bean<List<WeekCount>>> getWeekCount(@Query("time") String time, @Query("deptId") String deptId);

        @GET("task/unstation/week.json")
        Observable<Bean<List<WeekList>>> getWeekList(@Query("time") String time, @Query("deptId") String deptId);

        //本月未到岗统计
        @GET("task/unstation/month.json")
        Observable<Bean<List<MonthCount>>> getMonth(@Query("time") String time, @Query("deptId") String deptId);

        //获取部门id
        @GET("dept/list/depttype.json")
        Observable<Bean<List<DeptType>>> getDeptType(@Query("types") String types);

        //上报故障统计
        @GET("statistics/fault/report.json")
        Observable<Bean<List<FaultReport>>> getFaultReport(@Query("deptId") long deptId, @Query("time") String time);

        //故障等级统计
        @GET("statistics/user/fault/level.json")
        Observable<Bean<List<FaultLevel>>> getFaultLevel(@Query("time") String time);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("message/send/system.json")
        Observable<Bean<String>> sendMessage(@Body() String str);

        @GET("valueAdded/list.json")
        Observable<Bean<ValueAddedBean>> getValueAdded();

        @GET("statistics/work/stat.json")
        Observable<Bean<WorkState>> getWorkStat();

        @GET("statistics/today/todoList.json")
        Observable<Bean<List<TodayToDoBean>>> getTodayToList();

        @GET("statistics/work/statDetail.json")
        Observable<Bean<PartPersonStatistics>> getStatisticsUserAndPart(@Query("startTime") String startTime
                , @Query("endTime") String endTime
                , @Query("isPersonal") int type);

    }

    //巡检
    public interface Check {
        //巡检详情头部
        @GET("task/taskinfo.json")
        Observable<Bean<CheckBean>> getCheckInfo(@Query("taskId") long taskId);

        //故障列表
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultList(@Query("taskId") long taskId, @Query("count") int pageSize);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getMoreFaultList(@Query("count") int pageSize, @Query("taskId") long taskId, @Query("lastId") int lastId);

        //今日设备故障
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getTodayFault(@Query("count") int pageSize, @Query("startTime") String startTime, @Query("endTime") String endTime);

        //今日设备故障
        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getMoreTodayFault(@Query("count") int pageSize, @Query("lastId") long lastId, @Query("startTime") String startTime, @Query("endTime") String endTime);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultList(@Query("count") int pageSize, @Query("faultState") int faultState);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getFaultList(@Query("count") int pageSize, @Query("faultState") int faultState, @Query("lastId") long lastId);

        @GET("fault/list.json")
        Observable<Bean<List<FaultList>>> getAlarmList(@QueryMap() Map<String, String> map);

        //根据takid查询设备
        @GET("task/get/room/equipments.json")
        Observable<Bean<List<EquipBean>>> getEquipByTaskId(@Query("taskId") long taskId);
    }

    public interface NewVersionApi {

        @GET("version/latestCustomerApp.json")
        Observable<Bean<NewVersion>> newVersion();
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

    public interface UserUpdate {

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/appEdit.json")
        Observable<Bean<String>> updateUser(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/resetPwd.json")
        Observable<Bean<String>> updateUserPassWord(@Body() String info);

        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/sendCode.json")
        Observable<Bean<String>> sendCode(@Body() String info);

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

    public interface Getui {
        @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
        @POST("user/bind/usercid.json")
        Observable<Bean<String>> postCidInfo(@Body() String info);
    }

    public interface Stand {
        @GET("regulation/list.json")
        Observable<Bean<StandBean>> getStandInfo(@Query("count") int pageSize);
    }

    public interface InjectEquipmentApi {

        @GET("room/user/list.json")
        Observable<Bean<List<InjectRoomBean>>> getInjectRoom(@Query("roomType") int roomType);

        @GET("room/get/equipments.json")
        Observable<Bean<List<InjectEquipment>>> getInjectEquipment(@Query("roomId") long roomId);
    }
}
