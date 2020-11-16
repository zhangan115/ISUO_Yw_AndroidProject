package com.isuo.yw2application.mode.customer;


import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.EnterpriseCustomer;
import com.isuo.yw2application.mode.bean.NewVersion;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.check.CheckBean;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.count.ComeCount;
import com.isuo.yw2application.mode.bean.count.MonthCount;
import com.isuo.yw2application.mode.bean.count.PartPersonStatistics;
import com.isuo.yw2application.mode.bean.count.WeekCount;
import com.isuo.yw2application.mode.bean.count.WeekList;
import com.isuo.yw2application.mode.bean.discover.DeptType;
import com.isuo.yw2application.mode.bean.discover.FaultLevel;
import com.isuo.yw2application.mode.bean.discover.FaultReport;
import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.isuo.yw2application.mode.bean.discover.ValueAddedBean;
import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipRoom;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.fault.AlarmCount;
import com.isuo.yw2application.mode.bean.fault.FaultCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultDayCountBean;
import com.isuo.yw2application.mode.bean.fault.FaultYearCountBean;
import com.isuo.yw2application.mode.bean.news.EnterpriseDetailBean;
import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.isuo.yw2application.mode.bean.db.NewsBean;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * 接口定义M层
 * Created by zhangan on 2017-06-22.
 */

public interface CustomerDataSource {
    /**
     * 忽略该版本的更新
     *
     * @param version 版本
     */
    void ignoreVersion(NewVersion version);

    @NonNull
    Subscription updateUserPassWord(String oldPassWord, String newPassWord, @NonNull IObjectCallBack<String> callBack);

    //用户
    interface LoadUserCallBack {

        void onLoginSuccess(@NonNull User user);

        void onLoginFail();

        void onFinish();

        void showMessage(String message);

        void showFreezeMessage(String message);
    }

    interface AutoLoginCallBack {

        void onNeedLogin();

        void onAutoFinish();
    }

    interface SplashCallBack extends AutoLoginCallBack {

        void showWelcome();

    }

    interface NewVersionCallBack {

        void newVersion(NewVersion result);

        void noNewVersion();
    }

    @NonNull
    Subscription getNewVersion(@NonNull NewVersionCallBack callBack);

    /**
     * 登陆
     *
     * @param name     用户名
     * @param pass     用户密码
     * @param callBack 回调
     */
    @NonNull
    Subscription login(@NonNull String name, @NonNull String pass, @NonNull LoadUserCallBack callBack);

    /**
     * 自动登录
     *
     * @param callBack 回调
     */
    @NonNull
    Subscription autoLogin(@NonNull SplashCallBack callBack);

    //设备台账
    @NonNull
    Subscription getEquipInfo(boolean isFocus, @NonNull IListCallBack<EquipBean> callBack);

    //今日昨日到岗统计
    Subscription getTodayCount(@NonNull String time, @NonNull String deptId, @NonNull IListCallBack<ComeCount> callBack);

    //本周未到岗统计
    Subscription getWeekCount(@NonNull String time, @NonNull String deptId, @NonNull IListCallBack<WeekCount> callBack);

    Subscription getWeekList(@NonNull String time, @NonNull String deptId, @NonNull IListCallBack<WeekList> callBack);

    //本月未到岗统计
    Subscription getMonthCount(@NonNull String time, @NonNull String deptId, @NonNull IListCallBack<MonthCount> callBack);

    //获取设备类型
    Subscription getEquipType(@NonNull IListCallBack<EquipType> callBack);

    //获取配电室
    Subscription getEquipRoom(@NonNull IListCallBack<EquipRoom> callBack);

    //根据条件获取设备列表
    Subscription getEquipList(@NonNull Map<String, Object> map, IListCallBack<EquipmentBean> callBack);

    //获取巡检详情头部信息
    Subscription getCheckInfo(long taskId, IObjectCallBack<CheckBean> callBack);

    //获取巡检详情列表
    Subscription getFaultList(long taskId, IListCallBack<FaultList> callBack);

    Subscription getMoreFaultList(long taskId, int lastId, IListCallBack<FaultList> callBack);

    //今日设备故障
    Subscription getTodayFault(@NonNull String startTime, @NonNull String endTime, IListCallBack<FaultList> callBack);

    //今日设备故障
    @NonNull
    Subscription getTodayFault(int faultState, Long lastId, IListCallBack<FaultList> callBack);

    Subscription getMoreTodayFault(long lastId, @NonNull String startTime, @NonNull String endTime, IListCallBack<FaultList> callBack);

    @NonNull
    Subscription getFaultCount(@NonNull IObjectCallBack<FaultCountBean> callBack);

    @NonNull
    Subscription getFaultDayCount(@NonNull String time, @NonNull IObjectCallBack<FaultDayCountBean> callBack);

    @NonNull
    Subscription getFaultYearCount(@NonNull String time, @NonNull IObjectCallBack<FaultYearCountBean> callBack);

    //获取部门id
    Subscription getDeptTypeId(@NonNull String types, @NonNull IListCallBack<DeptType> callBack);

    //获取故障上报统计
    Subscription getFaultReport(long deptId, @NonNull String time, @NonNull IListCallBack<FaultReport> callBack);

    //故障等级统计
    Subscription getFaultLevel(@NonNull String time, @NonNull IListCallBack<FaultLevel> callBack);

    //根据taskid查询设备
    Subscription getEquipByTaskId(long taskId, @NonNull IListCallBack<EquipBean> callBack);

    @NonNull
    Subscription uploadUserPhoto(@NonNull File file, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription exitApp();

    @NonNull
    Subscription postQuestion(String title, String content, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getCode(@NonNull String phoneNum, @NonNull IObjectCallBack<String> callBack);


    @NonNull
    Subscription resetPwd(@NonNull String phone, @NonNull String userPwd, @NonNull String vCode, @NonNull IObjectCallBack<String> callBack);

    //发送cid
    Subscription postCidInfo(@NonNull String userCid, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getAlarmCount(@NonNull IObjectCallBack<AlarmCount> callBack);

    //获取规章制度
    Subscription getStandInfo(@NonNull IObjectCallBack<StandBean> callBack);

    @NonNull
    Subscription getAlarmList(Map<String, String> map, @NonNull IListCallBack<FaultList> callBack);

    @NonNull
    Subscription loadNewsFromDb(int newsType, @NonNull IListCallBack<NewsBean> callBack);

    @NonNull
    Subscription deleteNews(NewsBean newsBean);

    @NonNull
    Subscription uploadNewsBeen(List<NewsBean> newsBeen);

    @NonNull
    Subscription uploadNewsBean(NewsBean newsBean);

    long loadUnReadMessageCount();

    @NonNull
    Subscription getUnreadCount(@NonNull UnReadCountCallBack callBack);

    interface UnReadCountCallBack {
        void onReadCount(int[] count);
    }

    /**
     * 获取设备详情
     *
     * @param equipmentId 设备id
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    Subscription getEquipmentDetail(long equipmentId, @NonNull IObjectCallBack<EquipmentBean> callBack);

    @NonNull
    Subscription sendSystemMessage(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription uploadFile(List<String> filePaths, IObjectCallBack<String> callBack);

    @NonNull
    Subscription getValueAdded(IObjectCallBack<ValueAddedBean> callBack);

    @NonNull
    Subscription getPartData(String startTime, String endTime, IObjectCallBack<PartPersonStatistics> callBack);

    @NonNull
    Subscription getPerson(String startTime, String endTime, IObjectCallBack<PartPersonStatistics> callBack);

    @NonNull
    Subscription borrowedSure(long id, int state, IObjectCallBack<String> callBack);

    @NonNull
    Subscription getMessageList(Map<String, String> map, IListCallBack<MessageListBean> callBack);

    @NonNull
    Subscription getMessageDetail(long messageId, IObjectCallBack<EnterpriseDetailBean> callBack);

    void downLoadFile(String fileName, String filePath, String downLoadUrl, @NonNull DownLoadCallBack callBack);


    interface DownLoadCallBack {

        void onSuccess(String fileName, String filePath);

        void onFile();
    }

    @NonNull
    Subscription getFireSaveMessage(long messageId, IObjectCallBack<MessageListBean> callBack);

    /**
     * 获取关注设备数据
     *
     * @param equipmentId 设备id
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    Subscription getCareEquipmentData(long equipmentId, IObjectCallBack<FocusBean> callBack);

    @NonNull
    Subscription register(JSONObject json, IObjectCallBack<String> callBack);

    @NonNull
    Subscription getRegisterCode(String phoneNum, IObjectCallBack<String> callBack);

    @NonNull
    Subscription addCustomer(JSONObject json, IObjectCallBack<String> callBack);

    @NonNull
    Subscription addUserRegister(JSONObject json, IObjectCallBack<String> callBack);

    @NonNull
    Subscription getCustomerList(JSONObject json, IObjectCallBack<EnterpriseCustomer> callBack);

    @NonNull
    Subscription createCustomer(JSONObject json, IObjectCallBack<String> callBack);

    @NonNull
    Subscription joinCustomer(JSONObject json, IObjectCallBack<String> callBack);

}
