package com.sito.evpro.inspection.mode.inspection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.EmergencyCall;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.equip.EquipRoom;
import com.sito.evpro.inspection.mode.bean.equip.EquipType;
import com.sito.evpro.inspection.mode.bean.fault.CheckBean;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectResultBean;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.evpro.inspection.mode.bean.increment.IncreList;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.evpro.inspection.mode.bean.inspection.OperationBean;
import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.evpro.inspection.mode.bean.overhaul.RepairWorkBean;
import com.sito.evpro.inspection.mode.bean.overhaul.WorkBean;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * 接口定义M层
 * Created by zhangan on 2017-06-22.
 */

public interface InspectionDataSource {
    /**
     * 忽略该版本的更新
     *
     * @param version 版本
     */
    void ignoreVersion(NewVersion version);

    @NonNull
    Subscription postQuestion(String title, String content, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getEmergencyCalls(IListCallBack<EmergencyCall> iListCallBack);

    //用户
    interface LoadUserCallBack {

        void onLoginSuccess(@NonNull User user);

        void onLoginFail();

        void onFinish();
    }

    interface AutoLoginCallBack {

        void onNeedLogin();

        void onAutoFinish();
    }

    interface SplashCallBack extends AutoLoginCallBack {


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

    @NonNull
    Subscription getInspectionList(String data, IListCallBack<InspectionBean> callBack);

    @NonNull
    Subscription operationTask(String taskId, int position, IObjectCallBack<OperationBean> callBack);

    @NonNull
    Subscription getInspectionList(String data, String lastId, IListCallBack<InspectionBean> callBack);

    @NonNull
    Subscription getEquipInfo(@NonNull IListCallBack<EquipBean> callBack);

    @NonNull
    Subscription getOptionInfo(@NonNull IListCallBack<OptionBean> callBack);


    @NonNull
    Subscription getOverhaulList(String data, @NonNull IListCallBack<OverhaulBean> callBack);


    @NonNull
    Subscription getOverhaulList(String data, String lastId, @NonNull IListCallBack<OverhaulBean> callBack);


    @NonNull
    Subscription getIncrementList(String data, @NonNull IListCallBack<IncrementBean> callBack);

    @NonNull
    Subscription getIncrementList(String data, String lastId, @NonNull IListCallBack<IncrementBean> callBack);

    //历史故障
    Subscription getHistoryList(int count, @NonNull IListCallBack<FaultList> callBack);

    Subscription getMoreHistoryList(int count, long lastId, @NonNull IListCallBack<FaultList> callBack);

    //增值工作历史记录
    Subscription getIncrementHistoryList(int count, @NonNull IListCallBack<IncrementBean> callBack);

    Subscription getIncrementMoreHistoryList(int count, long lastId, @NonNull IListCallBack<IncrementBean> callBack);


    @NonNull
    Subscription getRepairWork(@NonNull String repairId, @NonNull IObjectCallBack<OverhaulBean> callBack);

    @NonNull
    Subscription loadRepairWorkFromDb(@NonNull String repairId, @NonNull IObjectCallBack<WorkBean> callBack);


    interface UploadRepairDataCallBack {

        void uploadSuccess();

        void uploadFail();
    }

    @NonNull
    Subscription uploadOverhaulData(@NonNull JSONObject jsonObject, @NonNull UploadRepairDataCallBack callBack);

    interface UploadPhotoCallBack {

        void onSuccess(@NonNull Image image);

        void onError();
    }

    //获取设备类型
    Subscription getEquipType(@NonNull IListCallBack<EquipType> callBack);

    //获取配电室
    Subscription getEquipRoom(@NonNull IListCallBack<EquipRoom> callBack);

    //根据条件获取设备列表
    Subscription getEquipList(@NonNull Map<String, Object> map, IListCallBack<EquipmentBean> callBack);

    //根据taskid查询设备
    Subscription getEquipByTaskId(long taskId, @NonNull IListCallBack<EquipBean> callBack);

    //获取巡检详情头部信息
    Subscription getCheckInfo(long taskId, IObjectCallBack<CheckBean> callBack);

    //获取巡检详情列表
    Subscription getFaultList(long taskId, IListCallBack<FaultList> callBack);

    Subscription getMoreFaultList(long taskId, int lastId, IListCallBack<FaultList> callBack);

    /**
     * 获取检修详情
     *
     * @param repairId 维修id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getRepairDetail(@NonNull String repairId, @NonNull IObjectCallBack<OverhaulBean> callBack);

    //安全包
    Subscription getSecureInfo(long securityId, @NonNull IObjectCallBack<SecureBean> callBack);

    @NonNull
    Subscription exitApp();

    @NonNull
    Subscription uploadUserPhoto(@NonNull File file, @NonNull IObjectCallBack<String> callBack);


    @NonNull
    Subscription updateUserPassWord(String oldPassWord, String newPassWord, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription getCode(@NonNull String phoneNum, @NonNull IObjectCallBack<String> callBack);


    @NonNull
    Subscription resetPwd(@NonNull String phone, @NonNull String userPwd, @NonNull String vCode, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription uploadSoSData( @NonNull JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    boolean getRepairState(@NonNull String repairId, @NonNull String noteId);

    void setRepairState(@NonNull String repairId, @NonNull String noteId);

    @NonNull
    Subscription getOverhaulNote(@NonNull String noteId, @NonNull IObjectCallBack<OverhaulNoteBean> callBack);

    @NonNull
    Subscription getInjectRoomList(int roomType, @NonNull IListCallBack<InjectRoomBean> callBack);

    @NonNull
    Subscription getInjectEquipmentList(long roomId, @NonNull IListCallBack<InjectEquipment> callBack);

    @NonNull
    Subscription injectEquipmentList(long equipmentId, Integer cycle, @NonNull IObjectCallBack<InjectResultBean> callBack);


    @NonNull
    Subscription startIncrement(long workId, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription startOverhaul(long repairId, @NonNull IObjectCallBack<String> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, @NonNull Image image, @NonNull UploadImageCallBack callBack);

    /**
     * 获取设备详情
     *
     * @param equipmentId 设备id
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    Subscription getEquipmentDetail(long equipmentId, @NonNull IObjectCallBack<EquipmentBean> callBack);

    interface UploadFileCallBack {

        void uploadSuccess(String url);

        void uploadFail();
    }

    @NonNull
    Subscription uploadFile(@NonNull String filePath, @NonNull String businessType
            , @NonNull String fileType, @NonNull UploadFileCallBack callBack);
}
