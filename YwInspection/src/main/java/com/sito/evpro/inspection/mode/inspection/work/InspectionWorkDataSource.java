package com.sito.evpro.inspection.mode.inspection.work;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDataBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadInspectionBean;

import java.util.List;

import rx.Subscription;

/**
 * 上传巡检数据
 * Created by zhangan on 2017-06-22.
 */

public interface InspectionWorkDataSource {

    interface UploadPhotoCallBack {

        void onSuccess();

        void onFail();

        void onFinish();
    }

    @NonNull
    Subscription uploadInspectionPhoto(@NonNull DataItemBean dataItemBean, @NonNull UploadPhotoCallBack callBack);

    @NonNull
    Subscription uploadRandomDataPhoto(@NonNull RoomDb roomDb, @NonNull UploadPhotoCallBack callBack);

    @NonNull
    Subscription getInspectionDetailList(@NonNull String taskId, @NonNull IObjectCallBack<InspectionDetailBean> callBack);

    interface LoadTaskUserCallBack {

        void onSuccess(@NonNull List<TaskDb> taskDb);

        void onError();
    }

    interface SaveRoomDbCallBack {
        void onSuccess();

        void onError();
    }


    @NonNull
    Subscription saveRoomDataToDb(long taskId, @NonNull RoomListBean roomListBean, @NonNull SaveRoomDbCallBack callBack);

    @NonNull
    Subscription loadTaskUserFromDb(long taskId, @NonNull LoadTaskUserCallBack callBack);

    @NonNull
    Subscription saveTaskUserToDb(long taskId, @NonNull List<EmployeeBean> employeeBeen);

    interface LoadRoomListDataCallBack {

        void onSuccess(RoomListBean roomListBean);

        void onError();
    }


    @NonNull
    Subscription loadRoomListDataFromDb(long taskId, RoomListBean roomListBean, @NonNull LoadRoomListDataCallBack callBack);

    interface UploadRoomListCallBack {

        void onSuccess();

        void noDataUpload();

        void onError();

    }

    @NonNull
    Subscription uploadRoomListData(int position, @NonNull List<TaskEquipmentBean> taskEquipmentBeans, @NonNull UploadRoomListCallBack callBack);

    //修改配电室状态
    @NonNull
    Subscription updateRoomState(long taskId, long taskRoomId, int operation, @NonNull IObjectCallBack<String> callBack);

    //修改任务的状态和人，包括领取，开始，完成，完成时将人员一起提交过来
    @NonNull
    Subscription updateTaskAll(long taskId, int operation, @NonNull String userIds, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription uploadUserPhotoInfo(long taskId, long equipmentId, @NonNull String url, final IObjectCallBack<String> callBack);

    interface RefreshRoomDataCallBack {

        void onSuccess(RoomDb roomDb);

        void onError();
    }

    @NonNull
    Subscription refreshRoomData(long taskId, RoomDb roomDb, RefreshRoomDataCallBack callBack);

    @NonNull
    Subscription saveEquipmentDataList(int position);

    @NonNull
    Subscription getInspectionData(@NonNull Long taskId, @NonNull IObjectCallBack<InspectionDataBean> callBack);

    boolean canUploadAllData(List<TaskEquipmentBean> taskEquipmentBeans);

    boolean roomUploadState(long taskId, long roomId);

    boolean checkPhotoInspectionData(List<TaskEquipmentBean> taskEquipmentBeans);

    interface IUploadPhotoCallBack {

        void onSuccess(String url);

        void onFinish();

        void onFail();
    }

    /**
     * 上传拍了照片但是没有上传的
     *
     * @param roomDb 完成时候拍的照片上传
     */
    void uploadPhotoList(RoomDb roomDb, IUploadOfflineCallBack callBack);

    /**
     * 上传离线保存的照片
     */
    interface IUploadOfflineCallBack {

        void onFinish();

        void onFail();
    }

    @NonNull
    Subscription saveInputData(TaskEquipmentBean taskEquipmentBean, boolean isAuto);
}

