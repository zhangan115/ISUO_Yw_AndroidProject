package com.isuo.yw2application.mode.create;

import android.support.annotation.NonNull;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.create.ChooseRoomOrType;
import com.isuo.yw2application.mode.bean.equip.EquipRoom;
import com.isuo.yw2application.mode.bean.equip.EquipType;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

/**
 * 创建台账
 * Created by zhangan on 2017/9/22.
 */

public class CreateRepository implements CreateDataSource {

    private CreateRepository() {

    }

    private static CreateRepository repository;

    public static CreateRepository getRepository() {
        if (repository == null) {
            repository = new CreateRepository();
        }
        return repository;
    }

    @Override
    public Subscription uploadPhoto(@NonNull String businessType, @NonNull String path, final @NonNull IObjectCallBack<List<String>> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                if (strings == null || strings.size() == 0) {
                    callBack.onError("上传失败");
                } else {
                    callBack.onSuccess(strings);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("上传失败");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getRoomList(@NonNull final IListCallBack<ChooseRoomOrType> callBack) {
        Observable<Bean<List<EquipRoom>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipPlace();
        return new ApiCallBack<List<EquipRoom>>(observable) {
            @Override
            public void onSuccess(List<EquipRoom> equipRooms) {
                callBack.onFinish();
                if (equipRooms == null || equipRooms.size() == 0) {
                    callBack.onError("");
                } else {
                    List<ChooseRoomOrType> resultList = new ArrayList<>();
                    for (int i = 0; i < equipRooms.size(); i++) {
                        resultList.add(new ChooseRoomOrType(equipRooms.get(i).getRoomName(), equipRooms.get(i).getRoomId()));
                    }
                    callBack.onSuccess(resultList);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getEquipmentTypeList(@NonNull final IListCallBack<ChooseRoomOrType> callBack) {
        Observable<Bean<List<EquipType>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipType();
        return new ApiCallBack<List<EquipType>>(observable) {
            @Override
            public void onSuccess(List<EquipType> equipTypes) {
                callBack.onFinish();
                if (equipTypes == null || equipTypes.size() == 0) {
                    callBack.onError("");
                } else {
                    List<ChooseRoomOrType> resultList = new ArrayList<>();
                    for (int i = 0; i < equipTypes.size(); i++) {
                        ChooseRoomOrType type = new ChooseRoomOrType(equipTypes.get(i).getEquipmentTypeName(), equipTypes.get(i).getEquipmentTypeId());
                        type.setLevel(equipTypes.get(i).getLevel());
                        type.setParentId(equipTypes.get(i).getParentId());
                        resultList.add(type);
                    }
                    callBack.onSuccess(resultList);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription addRoom(int roomType, @NonNull String name, final @NonNull IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(CreateApi.class)
                .addRoom(roomType, name);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String string) {
                callBack.onFinish();
                callBack.onSuccess(string);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription addEquipmentType(Long parentId, Integer level, @NonNull String name, @NonNull IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(CreateApi.class)
                .addEquipmentType(parentId,level,name);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String string) {
                callBack.onFinish();
                callBack.onSuccess(string);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription addEquipment(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(CreateApi.class)
                .addEquipment(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String string) {
                callBack.onFinish();
                callBack.onSuccess(string);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription editEquipment(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(CreateApi.class)
                .editEquipment(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String string) {
                callBack.onFinish();
                callBack.onSuccess(string);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription deleteEquipmentType(long equipmentTypeId, @NonNull final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(CreateApi.class).deleteEquipmentType(equipmentTypeId)) {
            @Override
            public void onSuccess(String s) {
                callBack.onFinish();
                callBack.onSuccess(s);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription deleteRoom(long roomId, @NonNull final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(CreateApi.class).deleteRoom(roomId)) {
            @Override
            public void onSuccess(String s) {
                callBack.onFinish();
                callBack.onSuccess(s);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }
}
