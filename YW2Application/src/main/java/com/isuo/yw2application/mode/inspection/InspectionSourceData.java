package com.isuo.yw2application.mode.inspection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.SecureBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 巡检逻辑
 * Created by zhangan on 2018/3/20.
 */

public interface InspectionSourceData {

    /**
     * 领取任务
     *
     * @param taskId   任务id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getOperationTask(@NonNull String taskId, @NonNull IObjectCallBack<String> callBack);

    /**
     * 从缓存中获取巡检数据
     *
     * @param inspectionType 巡检类型
     * @param data           时间
     * @param callBack       回调
     * @return 订阅
     */
    @NonNull
    Subscription getInspectionDataFromCache(int inspectionType, String data, IListCallBack<InspectionBean> callBack);

    /**
     * 保存数据到缓存中
     *
     * @param inspectionType         巡检类型
     * @param data                   时间
     * @param WorkInspectionBeanList 回调
     * @return 订阅
     */
    @NonNull
    Subscription saveInspectionDataToCache(int inspectionType, String data, List<InspectionBean> WorkInspectionBeanList);

    /**
     * 获取安全包
     *
     * @param securityId 安全包id
     * @param callBack   回调
     * @return 订阅
     */
    @NonNull
    Subscription getSecureInfo(long securityId, @NonNull IObjectCallBack<SecureBean> callBack);

    /**
     * 获取任务详情
     *
     * @param taskId   任务id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInspectionDetailList(long taskId, @NonNull IObjectCallBack<InspectionDetailBean> callBack);

    /**
     * 更新任务后保存到本地
     *
     * @param detailBean 数据
     */
    void saveInspectionDataToAcCache(@NonNull InspectionDetailBean detailBean);

    /**
     * 获取缓存的数据
     *
     * @param taskId 任务ID
     * @return 数据
     */
    @Nullable
    InspectionDetailBean getInspectionDataFromAcCache(long taskId);

    /**
     * 获取巡检列表
     *
     * @param data     时间
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription getInspectionData(int inspectionType, @NonNull String data, @Nullable String lastId, @NonNull IListCallBack<InspectionBean> callBack);

    //从数据库获取任务执行人回调
    interface LoadTaskUserCallBack {

        /**
         * 成功
         *
         * @param taskDb 保存用户对象
         */
        void onSuccess(@NonNull ArrayList<TaskDb> taskDb);

        void onError();
    }

    /**
     * 从数据库获取任务执行人
     *
     * @param taskId   任务id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription loadTaskUserFromDb(long taskId, @NonNull LoadTaskUserCallBack callBack);

    /**
     * 保存任务执行人
     *
     * @param taskId       任务id
     * @param employeeBeen 选择的执行人
     * @return 订阅
     */
    @NonNull
    Subscription saveTaskUserToDb(long taskId, @NonNull List<EmployeeBean> employeeBeen);

    /**
     * 更新任务配电室状态
     *
     * @param taskId       任务id
     * @param roomListBean 配电室
     * @param operation    状态
     * @param callBack     回调
     * @return 订阅
     */
    @NonNull
    Subscription updateRoomState(long taskId, RoomListBean roomListBean, int operation, @NonNull final IObjectCallBack<String> callBack);

    //读取配电室数据回调
    interface LoadRoomDataCallBack {

        /**
         * 成功
         *
         * @param list 配电室数据
         */
        void onSuccess(List<RoomListBean> list);

        void onError();
    }

    /**
     * 从数据库获取配电室信息
     *
     * @param taskId   任务id
     * @param list     配电室集合
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription loadRoomDataFromDb(long taskId, List<RoomListBean> list, @NonNull LoadRoomDataCallBack callBack);

    //获取配电室信息回调
    interface LoadRoomListDataCallBack {

        /**
         * 成功
         *
         * @param roomListBean 配电室集合
         */
        void onSuccess(RoomListBean roomListBean);

        void onError();
    }

    /**
     * 从数据库获取配电室信息
     *
     * @param taskId       任务id
     * @param roomListBean 配电室
     * @param callBack     回调
     * @return 订阅
     */
    @NonNull
    Subscription loadRoomListDataFromDb(long taskId, RoomListBean roomListBean, @NonNull LoadRoomListDataCallBack callBack);

    //上传配电室信息
    interface UploadRoomListCallBack {

        void onSuccess();

        void noDataUpload();

        void onError();

    }

    //上传任务数据
    interface UploadTaskCallBack {

        void onSuccess(@Nullable List<User> users);

        void noDataUpload();

        void onError();

    }

    /**
     * 上传设备数据
     *
     * @param position   位置
     * @param detailBean 巡检数据
     * @param callBack   回调
     * @return 订阅
     */
    @NonNull
    Subscription uploadTaskEquipmentListData(int position, InspectionDetailBean detailBean, TaskEquipmentBean equipmentBean, @NonNull UploadRoomListCallBack callBack);

    //上传图片回调
    interface UploadPhotoCallBack {

        void onSuccess();

        void onFail();

        void onFinish();
    }

    /**
     * 上传巡检照片
     *
     * @param dataItemBean 巡检数据对象
     * @param callBack     回调
     * @return 订阅
     */
    @NonNull
    Subscription uploadInspectionPhoto(@NonNull DataItemBean dataItemBean, @NonNull UploadPhotoCallBack callBack);

    /**
     * 保存输入类型数据
     *
     * @param taskEquipmentBean 巡检设备
     * @param isAuto            是否自动
     */
    void saveInputData(TaskEquipmentBean taskEquipmentBean, boolean isAuto);

    //上传照片回调
    interface IUploadPhotoCallBack {
        /**
         * @param url 成功地址
         */
        void onSuccess(String url);

        void onFinish();

        void onFail();
    }

    /**
     * 上传离线保存的照片
     */
    interface IUploadOfflineCallBack {

        void onFinish();

        void onFail();
    }

    /**
     * 上传任务数据
     *
     * @param task     任务
     * @param callBack 回调
     */
    void startUploadTask(InspectionBean task, @Nullable RoomListBean roomListBean, @NonNull UploadTaskCallBack callBack);

    /**
     * 上传任务数据
     *
     * @param task         任务
     * @param roomListBean 配电室
     * @param callBack     回调
     */
    void startRoomUploadTask(InspectionDetailBean task, @Nullable RoomListBean roomListBean, @NonNull UploadTaskCallBack callBack);

    /**
     * 更新用户照片
     *
     * @param taskId      任务id
     * @param equipmentId 设备id
     * @param url         地址
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    Subscription uploadRandomPhotoInfo(long taskId, long equipmentId, @NonNull final String url, final IObjectCallBack<String> callBack);

    /**
     * 上传随机照片
     *
     * @param roomDb   配电室数据库保存对象
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription uploadRandomDataPhoto(@NonNull final RoomDb roomDb, final @NonNull UploadPhotoCallBack callBack);

    /**
     * 修改任务的状态和人，包括领取，开始，完成，完成时将人员一起提交过来
     *
     * @param taskId    任务id
     * @param operation 状态
     * @param userIds   用户ids
     * @param callBack  回调
     * @return 订阅
     */
    @NonNull
    Subscription roomListFinish(long taskId, int operation, @NonNull String userIds, @NonNull IObjectCallBack<String> callBack);

    /**
     * 获取关注设备数据
     *
     * @param equipmentId 设备id
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    Subscription getCareEquipmentData(long equipmentId, IObjectCallBack<FocusBean> callBack);

    /**
     * 获取共享数据
     *
     * @param taskEquipmentBean 巡检对象
     * @param callBack          回调
     * @return 订阅
     */
    @NonNull
    Subscription getShareData(TaskEquipmentBean taskEquipmentBean, IShareDataCallBack callBack);

    //分享数据回调
    interface IShareDataCallBack {

        void onSuccess();

        void onFail();
    }

    /**
     * 获取保存的设备信息
     *
     * @return 设备
     */
    @NotNull
    Subscription getTaskEquipmentData(@NotNull IGetTaskEquipmentCallBack callBack);

    interface IGetTaskEquipmentCallBack {

        void onGetData(TaskEquipmentBean taskEquipmentBean);

        void noData();
    }

    /**
     * 保存设备到repository
     *
     * @param taskEquipmentBean 设备
     */
    void saveTaskEquipToRepository(TaskEquipmentBean taskEquipmentBean);

    @Nullable
    TaskEquipmentBean getTskEquipFromRepository();


    /**
     * 获取设备完成的数量
     *
     * @return 数量
     */
    int getEquipmentFinishCount(long taskId, RoomListBean roomListBean);


    /**
     * 获取设备录入项的完成数量
     *
     * @return 数量
     */
    long getEquipmentInputCount(long taskId, long roomId, long equipmentId);

    /**
     * 获取设备的完成状态
     *
     * @param taskId      taskId
     * @param roomId      roomId
     * @param equipmentId equipmentId
     * @return 是否完成
     */
    boolean getEquipmentFinishState(long taskId, long roomId, long equipmentId);
}
