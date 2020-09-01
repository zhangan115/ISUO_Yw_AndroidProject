package com.isuo.yw2application.mode.fault;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.api.FaultApi;
import com.isuo.yw2application.api.WorkApi;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.UploadImageCallBack;
import com.isuo.yw2application.mode.UploadResult;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.bean.db.VoiceDao;
import com.isuo.yw2application.mode.bean.employee.DepartmentBean;
import com.isuo.yw2application.mode.bean.equip.EquipType;
import com.isuo.yw2application.mode.bean.fault.DefaultFlowBean;
import com.isuo.yw2application.mode.bean.fault.FaultDetail;
import com.isuo.yw2application.mode.bean.fault.JobPackageBean;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

/**
 * fault
 * Created by zhangan on 2017-07-17.
 */
@Singleton
public class FaultRepository implements FaultDataSource {

    private SharedPreferences sp;

    @Inject
    public FaultRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Subscription getEquipmentType(@NonNull final IListCallBack<EquipType> callBack) {
        Observable<Bean<List<EquipType>>> observable = Api.createRetrofit().create(WorkApi.class).getEquipmentTypes();
        return new ApiCallBack<List<EquipType>>(observable) {

            @Override
            public void onSuccess(@Nullable List<EquipType> equipmentTypeBeen) {
                callBack.onFinish();
                if (equipmentTypeBeen == null) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(equipmentTypeBeen);
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
    public Subscription getFaultList(@Nullable String equipmentType, @Nullable String alarmType, @Nullable String alarmState
            , @Nullable String startTimeStr, @Nullable String endTimeStr, @Nullable String lastId, @NonNull final IListCallBack<FaultList> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("equipmentType", equipmentType);
            jsonObject.put("faultType", alarmType);
            jsonObject.put("faultState", alarmState);
            jsonObject.put("startTime", startTimeStr);
            jsonObject.put("endTime", endTimeStr);
            jsonObject.put("endTime", endTimeStr);
            jsonObject.put("count", ConstantInt.PAGE_SIZE);
            jsonObject.put("lastId", lastId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(FaultApi.class).getFaultList(jsonObject.toString());
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists == null || faultLists.size() == 0) {
                    callBack.onError("");
                    return;
                }
                callBack.onSuccess(faultLists);
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
    public Subscription getFaultDetail(String faultId, @NonNull final IObjectCallBack<FaultDetail> callBack) {
        Observable<Bean<FaultDetail>> observable = Api.createRetrofit().create(FaultApi.class).getFaultDetail(faultId);
        return new ApiCallBack<FaultDetail>(observable) {
            @Override
            public void onSuccess(@Nullable FaultDetail faultDetail) {
                callBack.onFinish();
                if (faultDetail != null) {
                    callBack.onSuccess(faultDetail);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    private List<DepartmentBean> departmentBeen = null;

    @NonNull
    @Override
    public Subscription getEmployeeList(Integer type, final @NonNull IListCallBack<DepartmentBean> callBack) {
        if (departmentBeen != null) {
            callBack.onSuccess(departmentBeen);
            return Observable.just(null).subscribe();
        }
        Observable<Bean<List<DepartmentBean>>> observable = Api.createRetrofit().create(FaultApi.class)
                .getEmployeeList(type);
        return new ApiCallBack<List<DepartmentBean>>(observable) {
            @Override
            public void onSuccess(List<DepartmentBean> been) {
                callBack.onFinish();
                if (been != null && been.size() > 0) {
                    callBack.onSuccess(been);
                } else {
                    callBack.onError("");
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
    public Subscription closeFault(Map<String, String> map, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(FaultApi.class).getCloseFault(map);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
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
    public Subscription flowFault(Map<String, String> map, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(FaultApi.class).getFlowFault(map);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
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
    public Subscription overhaulFault(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(FaultApi.class).repairAdd(jsonObject.toString());
        return new ApiCallBack<String>(observable) {

            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
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
    public Subscription getJobPackage(@NonNull final IObjectCallBack<JobPackageBean> callBack) {
        Observable<Bean<JobPackageBean>> observable = Api.createRetrofit().create(FaultApi.class).getJobPackage(ConstantInt.MAX_PAGE_SIZE);
        return new ApiCallBack<JobPackageBean>(observable) {

            @Override
            public void onSuccess(JobPackageBean been) {
                callBack.onFinish();
                callBack.onSuccess(been);
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
    public Subscription postFaultInfo(JSONObject jsonObject, @NonNull final IObjectCallBack<UploadResult> callBack) {
        Observable<Bean<UploadResult>> observable = Api.createRetrofit().create(FaultApi.class)
                .upLoadFault(jsonObject.toString());
        return new ApiCallBack<UploadResult>(observable) {
            @Override
            public void onSuccess(UploadResult uploadResult) {
                callBack.onFinish();
                callBack.onSuccess(uploadResult);

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
    public Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, @NonNull final Image image
            , @NonNull final UploadImageCallBack callBack) {
        image.setSaveTime(System.currentTimeMillis());
        image.setWorkType(workType);
        if (itemId != null) {
            image.setItemId(itemId);
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(image.getImageLocal());
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
                    Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                    callBack.onError(image);
                } else {
                    image.setIsUpload(true);
                    image.setBackUrl(strings.get(0));
                    Yw2Application.getInstance().getDaoSession().getImageDao().insertOrReplaceInTx(image);
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                Yw2Application.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                callBack.onFinish();
                callBack.onError(image);
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        QueryBuilder qb = Yw2Application.getInstance().getDaoSession().getVoiceDao().queryBuilder();
        qb.where(VoiceDao.Properties.WorkType.eq(workType), VoiceDao.Properties.IsUpload.eq(false));
        final List<Voice> voices = qb.list();
        for (int i = 0; i < voices.size(); i++) {
            File file = new File(voices.get(i).getVoiceLocal());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file" + i, file.getName(), requestFile);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(FaultApi.class)
                .postVoiceFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> fileBeen) {
                callBack.onFinish();
                if (fileBeen == null || fileBeen.size() == 0) {
                    callBack.onError("上传失败!");
                } else {
                    callBack.onSuccess(fileBeen);
                    for (int i = 0; i < fileBeen.size(); i++) {
                        Voice voice = voices.get(i);
                        voice.setBackUrl(fileBeen.get(i));
                        Yw2Application.getInstance().getDaoSession().getVoiceDao().update(voice);
                    }
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("上传失败!");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getDefaultFlow(@NonNull final IListCallBack<DefaultFlowBean> callBack) {
        Observable<Bean<List<DefaultFlowBean>>> observable = Api.createRetrofit().create(FaultApi.class).getDefaultFlow(1);
        return new ApiCallBack<List<DefaultFlowBean>>(observable) {
            @Override
            public void onSuccess(List<DefaultFlowBean> defaultFlowBeen) {
                callBack.onFinish();
                if (defaultFlowBeen != null && defaultFlowBeen.size() > 0) {
                    callBack.onSuccess(defaultFlowBeen);
                } else {
                    callBack.onError("");
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
    public Subscription getHistoryList(int count, @NonNull final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(FaultApi.class)
                .getFaultHistoryList(count, String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId())
                        , "0");
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(List<FaultList> faultLists) {
                callBack.onFinish();
                callBack.onSuccess(faultLists);
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
    public Subscription getMoreHistoryList(int count, long lastId, @NonNull final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(FaultApi.class)
                .getMoreFaultHistoryList(lastId, count
                        , String.valueOf(Yw2Application.getInstance().getCurrentUser().getUserId())
                        , "0");
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists.size() < ConstantInt.PAGE_SIZE) {
                    callBack.onSuccess(faultLists);
                    callBack.onError("");
                } else {
                    callBack.onSuccess(faultLists);
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
    public Subscription careEquipment(JSONObject jsonObject, @NonNull  final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(FaultApi.class).careEquipment(jsonObject.toString());
        return new ApiCallBack<String>(observable) {

            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

}
