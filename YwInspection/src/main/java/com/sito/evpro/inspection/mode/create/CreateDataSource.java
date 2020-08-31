package com.sito.evpro.inspection.mode.create;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.create.ChooseRoomOrType;
import com.sito.evpro.inspection.mode.bean.db.CreateEquipmentDb;
import com.sito.evpro.inspection.mode.bean.db.CreateRoomDb;

import org.json.JSONObject;

import java.util.List;

import rx.Subscription;

/**
 * 创建设备
 * Created by zhangan on 2017/9/22.
 */

public interface CreateDataSource {

    Subscription uploadPhoto(@NonNull String businessType, @NonNull String path, final @NonNull IObjectCallBack<List<String>> callBack);

    interface SaveCallBack {

        void onSaveSuccess();

        void onSaveError(@Nullable String message);
    }

    /**
     * 保存新建的区域
     *
     * @param createRoomDb 区域对象
     * @return 订阅
     */
    @NonNull
    Subscription saveRoom(CreateRoomDb createRoomDb, @Nullable SaveCallBack callBack);


    /**
     * 保存新建的设备
     *
     * @param createEquipmentDb 设备对象
     * @return 订阅
     */
    @NonNull
    Subscription saveEquipment(CreateEquipmentDb createEquipmentDb, @Nullable SaveCallBack callBack);

    /**
     * 设置新建设备保存的区域
     *
     * @param createRoomDb 区域
     */
    void setCreateRoomDb(@NonNull CreateRoomDb createRoomDb);

    /**
     * 获取新建设备保存的区域
     *
     * @return 区域
     */
    @NonNull
    CreateRoomDb getCreateRoomDb();

    @NonNull
    Subscription getRoomList(@NonNull IListCallBack<ChooseRoomOrType> callBack);

    @NonNull
    Subscription getEquipmentTypeList(@NonNull IListCallBack<ChooseRoomOrType> callBack);

    @NonNull
    Subscription addRoom(int roomType, @NonNull String name, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription addEquipmentType(@NonNull String name, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription addEquipment(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription editEquipment(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription deleteEquipmentType(long equipmentTypeId, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription deleteRoom(long roomId, @NonNull IObjectCallBack<String> callBack);
}
