package com.sito.evpro.inspection.mode.create;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.api.ApiCallBack;
import com.sito.evpro.inspection.api.CreateApi;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.create.ChooseRoomOrType;
import com.sito.evpro.inspection.mode.bean.db.CreateEquipmentDb;
import com.sito.evpro.inspection.mode.bean.db.CreateRoomDb;
import com.sito.evpro.inspection.mode.bean.db.CreateRoomDbDao;
import com.sito.evpro.inspection.mode.bean.equip.EquipRoom;
import com.sito.evpro.inspection.mode.bean.equip.EquipType;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 创建台账
 * Created by zhangan on 2017/9/22.
 */
@Singleton
public class CreateRepository implements CreateDataSource {

    private CreateRoomDb mCurrentRoomDb;

    @Inject
    public CreateRepository() {

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
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription saveRoom(CreateRoomDb createRoomDb, @Nullable final SaveCallBack callBack) {
        return Observable.just(createRoomDb).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).flatMap(new Func1<CreateRoomDb, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(CreateRoomDb createRoomDb) {
                        Boolean isSaveSuccess;//保存区域
                        if (InspectionApp.getInstance().getDaoSession().getCreateRoomDbDao().queryBuilder()
                                .where(CreateRoomDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                        , CreateRoomDbDao.Properties.RoomName.eq(createRoomDb.getRoomName())).list().size() > 0) {
                            isSaveSuccess = false;
                        } else {
                            InspectionApp.getInstance().getDaoSession().getCreateRoomDbDao().insertOrReplaceInTx(createRoomDb);
                            isSaveSuccess = true;
                        }
                        return Observable.just(isSaveSuccess);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean isSaveSuccess) {
                        if (callBack == null) {
                            return;
                        }
                        if (isSaveSuccess) {
                            callBack.onSaveSuccess();
                        } else {
                            callBack.onSaveError("存在同名区域");
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Subscription saveEquipment(CreateEquipmentDb createEquipmentDb, @Nullable SaveCallBack callBack) {
        return null;
    }

    @Override
    public void setCreateRoomDb(@NonNull CreateRoomDb createRoomDb) {
        mCurrentRoomDb = createRoomDb;
    }

    @NonNull
    @Override
    public CreateRoomDb getCreateRoomDb() {
        return mCurrentRoomDb;
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
        }.execute().subscribe();
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
                        resultList.add(new ChooseRoomOrType(equipTypes.get(i).getEquipmentTypeName(), equipTypes.get(i).getEquipmentTypeId()));
                    }
                    callBack.onSuccess(resultList);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
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
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription addEquipmentType(@NonNull String name, final @NonNull IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(CreateApi.class)
                .addEquipmentType(name);
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
        }.execute().subscribe();
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
        }.execute().subscribe();
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
        }.execute().subscribe();
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
        }.execute().subscribe();
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
        }.execute().subscribe();
    }
}
