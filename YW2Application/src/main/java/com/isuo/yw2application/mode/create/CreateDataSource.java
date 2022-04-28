package com.isuo.yw2application.mode.create;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;

import org.json.JSONObject;

import java.util.List;

import rx.Subscription;

/**
 * 创建对象
 * Created by zhangan on 2017/9/22.
 */

public interface CreateDataSource {

    Subscription uploadPhoto(@NonNull String businessType, @NonNull String path, final @NonNull IObjectCallBack<List<String>> callBack);

    interface SaveCallBack {

        void onSaveSuccess();

        void onSaveError(String message);
    }

    @NonNull
    Subscription getRoomList(@NonNull IListCallBack<ChooseRoomOrType> callBack);

    @NonNull
    Subscription getEquipmentTypeList(@NonNull IListCallBack<ChooseRoomOrType> callBack);

    @NonNull
    Subscription addRoom(int roomType, @NonNull String name, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription addEquipmentType(Long parentId, Integer level,@NonNull String name, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription addEquipment(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription editEquipment(JSONObject jsonObject, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription deleteEquipmentType(long equipmentTypeId, @NonNull IObjectCallBack<String> callBack);

    @NonNull
    Subscription deleteRoom(long roomId, @NonNull IObjectCallBack<String> callBack);
}
