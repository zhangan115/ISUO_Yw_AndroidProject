package com.sito.customer.mode.create;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.create.ChooseRoomOrType;

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

        void onSaveError(String message);
    }

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
