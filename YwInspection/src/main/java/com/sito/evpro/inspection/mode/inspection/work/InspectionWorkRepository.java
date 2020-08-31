package com.sito.evpro.inspection.mode.inspection.work;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.api.ApiCallBack;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDbDao;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDb;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDbDao;
import com.sito.evpro.inspection.mode.bean.db.RoomDb;
import com.sito.evpro.inspection.mode.bean.db.RoomDbDao;
import com.sito.evpro.inspection.mode.bean.db.TaskDb;
import com.sito.evpro.inspection.mode.bean.db.TaskDbDao;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.DataItemValueListBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDataBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.inspection.RoomListBean;
import com.sito.evpro.inspection.mode.bean.inspection.TaskEquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadDataItemBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadDataItemValueListBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadDataListBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadEquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadInspectionBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadRoomListBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadTaskEquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.upload.UploadTaskInfo;
import com.sito.library.utils.CalendarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 上传巡检数据
 * Created by zhangan on 2017-07-10.
 */
@Singleton
public class InspectionWorkRepository implements InspectionWorkDataSource {

    private SharedPreferences sp;

    @Inject
    public InspectionWorkRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Subscription uploadInspectionPhoto(@NonNull final DataItemBean dataItemBean, @NonNull final UploadPhotoCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "task")
                .addFormDataPart("fileType", "image");
        String filePath = null;
        if (dataItemBean.getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3) {
            filePath = dataItemBean.getEquipmentDataDb().getLocalPhoto();
        }
        if (TextUtils.isEmpty(filePath)) {
            dataItemBean.setUploading(false);
            callBack.onFail();
            return Observable.just(dataItemBean).subscribe();
        }
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                dataItemBean.setUploading(false);
                if (strings != null && strings.size() == 1) {
                    dataItemBean.setValue(strings.get(0));
                    dataItemBean.getEquipmentDataDb().setValue(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplace(dataItemBean.getEquipmentDataDb());
                    callBack.onSuccess();
                } else {
                    callBack.onFail();
                }
                callBack.onFinish();
            }

            @Override
            public void onFail() {
                dataItemBean.setUploading(false);
                dataItemBean.setValue(null);
                dataItemBean.getEquipmentDataDb().setValue(null);
                InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplace(dataItemBean.getEquipmentDataDb());
                callBack.onFinish();
                callBack.onFail();
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription uploadRandomDataPhoto(@NonNull final RoomDb roomDb, final @NonNull UploadPhotoCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "task")
                .addFormDataPart("fileType", "image");
        String filePath = roomDb.getPhotoUrl();
        if (TextUtils.isEmpty(filePath)) {
            callBack.onFail();
            return Observable.just(roomDb).subscribe();
        }
        File file = new File(filePath);
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
                if (strings != null && strings.size() == 1) {
                    roomDb.setUploadPhotoUrl(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplace(roomDb);
                    callBack.onSuccess();
                } else {
                    callBack.onFail();
                }
            }

            @Override
            public void onFail() {
                roomDb.setUploadPhotoUrl(null);
                InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplace(roomDb);
                callBack.onFinish();
                callBack.onFail();
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription saveRoomDataToDb(final long taskId, @NonNull final RoomListBean roomListBean, @NonNull final SaveRoomDbCallBack callBack) {
        return InspectionApp.getInstance().getDaoSession().getRoomDbDao().queryBuilder()
                .where(RoomDbDao.Properties.TaskId.eq(taskId), RoomDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                        , RoomDbDao.Properties.RoomId.eq(roomListBean.getTaskRoomId()))
                .rx()
                .unique()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<RoomDb>() {
                    @Override
                    public void call(RoomDb roomDb) {
                        if (roomDb == null) {
                            roomDb = new RoomDb();
                            roomDb.setTaskId(taskId);
                            roomDb.setUserId(InspectionApp.getInstance().getCurrentUser().getUserId());
                            roomDb.setRoomId(roomListBean.getTaskRoomId());
                        }
                        roomDb.setLastSaveTime(roomListBean.getLastSaveTime());
                        roomDb.setState(roomListBean.getState());
                        InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomDb>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError();
                    }

                    @Override
                    public void onNext(RoomDb roomDb) {
                        callBack.onSuccess();
                    }
                });
    }

    @NonNull
    @Override
    public Subscription loadTaskUserFromDb(final long taskId, @NonNull final LoadTaskUserCallBack callBack) {
        return InspectionApp.getInstance().getDaoSession().getTaskDbDao()
                .queryBuilder()
                .where(TaskDbDao.Properties.CurrectUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                        , TaskDbDao.Properties.TaskId.eq(taskId))
                .rx()
                .list()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<TaskDb>>() {
                    @Override
                    public void call(List<TaskDb> taskDbs) {
                        if (taskDbs.size() == 0) {
                            taskDbs.add(new TaskDb(taskId, InspectionApp.getInstance().getCurrentUser().getUserId()
                                    , InspectionApp.getInstance().getCurrentUser().getRealName()));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TaskDb>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<TaskDb> taskDbs) {
                        callBack.onSuccess(taskDbs);
                    }
                });
    }

    @NonNull
    @Override
    public Subscription saveTaskUserToDb(final long taskId, @NonNull final List<EmployeeBean> employeeBeen) {
        return InspectionApp.getInstance().getDaoSession().getTaskDbDao()
                .queryBuilder()
                .where(TaskDbDao.Properties.CurrectUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                        , TaskDbDao.Properties.TaskId.eq(taskId))
                .rx()
                .list()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<List<TaskDb>>() {
                    @Override
                    public void call(List<TaskDb> taskDbs) {
                        if (taskDbs != null && taskDbs.size() > 0) {
                            InspectionApp.getInstance().getDaoSession().getTaskDbDao().deleteInTx(taskDbs);
                        }
                        List<TaskDb> taskList = new ArrayList<>();
                        for (int i = 0; i < employeeBeen.size(); i++) {
                            taskList.add(new TaskDb(taskId, employeeBeen.get(i).getUser().getUserId(), employeeBeen.get(i).getUser().getRealName()));
                        }
                        InspectionApp.getInstance().getDaoSession().getTaskDbDao().insertOrReplaceInTx(taskList);
                    }
                })
                .subscribe();
    }

    @NonNull
    @Override
    public Subscription updateRoomState(long taskId, long taskRoomId, int operation, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
                .uploadStartOrEnd(taskId, taskRoomId, operation);
        return new ApiCallBack<String>(observable) {

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
    public Subscription updateTaskAll(long taskId, int operation, @NonNull String userIds, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
                .uploadTaskAll(taskId, operation, userIds);
        return new ApiCallBack<String>(observable) {
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
    public Subscription uploadUserPhotoInfo(long taskId, long equipmentId, @NonNull final String url, final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("taskId", taskId);
            jsonObject.put("equipmentId", equipmentId);
            jsonObject.put("inplacePicUrl", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Inspection.class).uploadUserPhoto(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String stringBean) {
                callBack.onFinish();
                callBack.onSuccess(url);
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
    public Subscription refreshRoomData(long taskId, RoomDb roomDb, final RefreshRoomDataCallBack callBack) {
        return InspectionApp.getInstance().getDaoSession().getRoomDbDao().queryBuilder()
                .where(RoomDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                        , RoomDbDao.Properties.TaskId.eq(taskId)
                        , RoomDbDao.Properties.RoomId.eq(roomDb.getRoomId()))
                .rx()
                .unique()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomDb>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RoomDb roomDb) {
                        callBack.onSuccess(roomDb);
                    }
                });
    }

    @NonNull
    @Override
    public Subscription saveEquipmentDataList(int position) {
        final InspectionDetailBean detailBean = InspectionApp.getInstance().getInspectionDetailBean();
        if (detailBean == null) {
            return Observable.just(position).subscribe();
        }
        final RoomListBean roomDataList = detailBean.getRoomList().get(position);
        List<EquipmentDataDb> equipmentDataDbList = new ArrayList<>();
        for (int i = 0; i < roomDataList.getTaskEquipment().size(); i++) {
            for (int j = 0; j < roomDataList.getTaskEquipment().get(i).getDataList().get(0).getDataItemValueList().size(); j++) {
                if (roomDataList.getTaskEquipment().get(i).getDataList().get(0)
                        .getDataItemValueList().get(j).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2
                        || roomDataList.getTaskEquipment().get(i).getDataList().get(0)
                        .getDataItemValueList().get(j).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_4) {
                    equipmentDataDbList.add(roomDataList.getTaskEquipment().get(i).getDataList()
                            .get(0).getDataItemValueList().get(j).getDataItem().getEquipmentDataDb());
                }
            }
        }
        return InspectionApp.getInstance().getDaoSession()
                .getEquipmentDataDbDao()
                .rx()
                .insertOrReplaceInTx(equipmentDataDbList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Iterable<EquipmentDataDb>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Iterable<EquipmentDataDb> equipmentDataDbs) {

                    }
                });
    }

    @NonNull
    @Override
    public Subscription loadRoomListDataFromDb(final long taskId, @NonNull RoomListBean roomListBean, @NonNull final LoadRoomListDataCallBack callBack) {
        return Observable.just(roomListBean)
                .doOnNext(new Action1<RoomListBean>() {
                    @Override
                    public void call(RoomListBean roomListBean) {
                        String removeData = null;
                        if (InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                                && InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
                            for (int i = 0; i < InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                                if (InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("removeDataDate")) {
                                    removeData = InspectionApp.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
                                    break;
                                }
                            }
                        }
                        boolean isAutoSetValue = false;
                        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
                        if (!TextUtils.isEmpty(removeData)) {
                            isAutoSetValue = !removeData.contains(String.valueOf(CalendarUtil.getWeekInt(calendar)));
                        }
                        List<TaskEquipmentBean> taskEquipmentBean = roomListBean.getTaskEquipment();
                        long roomId = roomListBean.getTaskRoomId();
                        int uploadCount = 0;
                        List<EquipmentDb> equipmentDbToSave = new ArrayList<>();
                        List<EquipmentDataDb> equipmentDataDbToSave = new ArrayList<>();
                        for (int i = 0; i < taskEquipmentBean.size(); i++) {
                            long equipmentId = taskEquipmentBean.get(i).getTaskEquipmentId();
                            EquipmentDb equipmentDb;
                            equipmentDb = InspectionApp.getInstance().getDaoSession().getEquipmentDbDao().queryBuilder()
                                    .where(EquipmentDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                            , EquipmentDbDao.Properties.TaskId.eq(taskId)
                                            , EquipmentDbDao.Properties.RoomId.eq(roomId)
                                            , EquipmentDbDao.Properties.EquipmentId.eq(equipmentId))
                                    .unique();
                            if (equipmentDb == null) {
                                equipmentDb = new EquipmentDb();
                                equipmentDb.setAlarmState(false);
                                equipmentDb.setEquipmentId(taskEquipmentBean.get(i).getTaskEquipmentId());
                                equipmentDb.setRoomId(roomListBean.getTaskRoomId());
                                equipmentDb.setEquipmentName(taskEquipmentBean.get(i).getEquipment().getEquipmentName());
                                equipmentDb.setTaskId(taskId);
                                equipmentDbToSave.add(equipmentDb);
                                taskEquipmentBean.get(i).getEquipment().setEquipmentDb(equipmentDb);
                            } else {
                                taskEquipmentBean.get(i).getEquipment().setEquipmentDb(equipmentDb);
                            }
                            if (equipmentDb.getUploadState()) {
                                uploadCount++;
                            }
                            for (int j = 0; j < taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().size(); j++) {
                                DataItemValueListBean dataItemValueListBean = taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().get(j);
                                EquipmentDataDb equipmentData = InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().queryBuilder()
                                        .where(EquipmentDataDbDao.Properties.CurrentUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                                , EquipmentDataDbDao.Properties.EquipmentId.eq(equipmentId)
                                                , EquipmentDataDbDao.Properties.RoomId.eq(roomId)
                                                , EquipmentDataDbDao.Properties.TaskId.eq(taskId)
                                                , EquipmentDataDbDao.Properties.DataItemId.eq(dataItemValueListBean.getDataItemValueId())
                                                , EquipmentDataDbDao.Properties.Type.eq(dataItemValueListBean.getDataItem().getInspectionType()))
                                        .unique();
                                if (equipmentData == null) {
                                    equipmentData = new EquipmentDataDb();
                                    equipmentData.setRoomId(roomId);
                                    equipmentData.setTaskId(taskId);
                                    equipmentData.setEquipmentId(equipmentId);
                                    equipmentData.setDataItemId(dataItemValueListBean.getDataItemValueId());
                                    equipmentData.setType(dataItemValueListBean.getDataItem().getInspectionType());
                                    dataItemValueListBean.getDataItem().setEquipmentDataDb(equipmentData);
                                    if (isAutoSetValue) {
                                        switch (dataItemValueListBean.getDataItem().getInspectionType()) {
                                            case ConstantInt.DATA_VALUE_TYPE_1:
                                                String lastValue = dataItemValueListBean.getLastValue();
                                                if (!TextUtils.isEmpty(lastValue) && dataItemValueListBean.getDataItem().getInspectionItemOptionList() != null
                                                        && dataItemValueListBean.getDataItem().getInspectionItemOptionList().size() > 0) {
                                                    for (int k = 0; k < dataItemValueListBean.getDataItem().getInspectionItemOptionList().size(); k++) {
                                                        if (lastValue.equals(String.valueOf(dataItemValueListBean.getDataItem()
                                                                .getInspectionItemOptionList().get(k).getId()))) {
                                                            equipmentData.setValue(dataItemValueListBean.getLastValue());
                                                            equipmentData.setChooseInspectionName(dataItemValueListBean.getDataItem()
                                                                    .getInspectionItemOptionList().get(k).getOptionName());
                                                            dataItemValueListBean.getDataItem().setValue(dataItemValueListBean.getLastValue());
                                                            dataItemValueListBean.getDataItem().setChooseInspectionName(dataItemValueListBean.getDataItem()
                                                                    .getInspectionItemOptionList().get(k).getOptionName());
                                                            break;
                                                        }
                                                    }
                                                }
                                                break;
                                            case ConstantInt.DATA_VALUE_TYPE_2:
                                            case ConstantInt.DATA_VALUE_TYPE_4:
                                                if (!TextUtils.isEmpty(dataItemValueListBean.getLastValue())) {
                                                    equipmentData.setValue(dataItemValueListBean.getLastValue());
                                                    dataItemValueListBean.getDataItem().setValue(dataItemValueListBean.getLastValue());
                                                }
                                                break;
                                        }
                                    }
                                    equipmentDataDbToSave.add(equipmentData);
                                } else {
                                    DataItemBean dataItem = dataItemValueListBean.getDataItem();
                                    if (!TextUtils.isEmpty(equipmentData.getValue())) {
                                        dataItem.setValue(equipmentData.getValue());
                                    }
                                    if (!TextUtils.isEmpty(equipmentData.getChooseInspectionName())) {
                                        dataItem.setChooseInspectionName(equipmentData.getChooseInspectionName());
                                    }
                                    if (dataItemValueListBean.getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3
                                            && !TextUtils.isEmpty(equipmentData.getLocalPhoto())) {
                                        dataItem.setLocalFile(equipmentData.getLocalPhoto());
                                    }
                                    dataItem.setEquipmentDataDb(equipmentData);
                                }
                            }
                        }
                        roomListBean.getRoomDb().setCheckCount(uploadCount);
                        InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomListBean.getRoomDb());
                        if (equipmentDbToSave.size() > 0) {
                            InspectionApp.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(equipmentDbToSave);
                        }
                        if (equipmentDataDbToSave.size() > 0) {
                            InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(equipmentDataDbToSave);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RoomListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.onError();
                    }

                    @Override
                    public void onNext(RoomListBean roomListBean) {
                        callBack.onSuccess(roomListBean);
                    }
                });
    }

    private List<TaskEquipmentBean> needUploadEquip;

    @NonNull
    @Override
    public Subscription uploadRoomListData(int position, @NonNull List<TaskEquipmentBean> taskEquipmentBeans, @NonNull final UploadRoomListCallBack callBack) {
        final InspectionDetailBean detailBean = InspectionApp.getInstance().getInspectionDetailBean();
        if (detailBean == null) {
            callBack.noDataUpload();
            return Observable.just(null).subscribe();
        }
        final RoomListBean roomDataList = detailBean.getRoomList().get(position);
        needUploadEquip = new ArrayList<>();
        int uploadCount = 0;
        for (int i = 0; i < taskEquipmentBeans.size(); i++) {
            if (taskEquipmentBeans.get(i).getEquipment().getEquipmentDb() != null) {
                if (taskEquipmentBeans.get(i).getEquipment().getEquipmentDb().getUploadState()) {
                    ++uploadCount;
                }
            }
        }
        if (uploadCount == taskEquipmentBeans.size()) {
            callBack.onSuccess();
            return Observable.just(null).subscribe();
        }
        for (int i = 0; i < taskEquipmentBeans.size(); i++) {
            boolean needUpload = true;
            for (int j = 0; j < taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().size(); j++) {
                if (TextUtils.isEmpty(taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().get(j).getDataItem().getValue())) {
                    needUpload = false;
                    break;
                }
            }
            if (needUpload) {
                if (!taskEquipmentBeans.get(i).getEquipment().getEquipmentDb().getUploadState()
                        && taskEquipmentBeans.get(i).getEquipment().getEquipmentDb().getCanUpload()) {
                    needUploadEquip.add(taskEquipmentBeans.get(i));
                }
            }
        }
        if (needUploadEquip.size() == 0) {
            callBack.noDataUpload();
            return Observable.just(null).subscribe();
        }
        List<UploadTaskEquipmentBean> uploadEquipList = new ArrayList<>();
        for (int i = 0; i < needUploadEquip.size(); i++) {
            TaskEquipmentBean taskEquipmentBean = needUploadEquip.get(i);
            List<UploadDataListBean> uploadDataListBeen = new ArrayList<>();
            for (int j = 0; j < taskEquipmentBean.getDataList().size(); j++) {
                List<UploadDataItemValueListBean> dataItemValueList = new ArrayList<>();
                for (int k = 0; k < taskEquipmentBean.getDataList().get(j).getDataItemValueList().size(); k++) {
                    DataItemValueListBean dataItemValueListBean = taskEquipmentBean.getDataList().get(j).getDataItemValueList().get(k);
                    DataItemBean dataItemBean = dataItemValueListBean.getDataItem();
                    UploadDataItemBean uploadDataItemBean = new UploadDataItemBean(dataItemBean.getInspectionId(),
                            dataItemBean.getCreateTime(), dataItemBean.getDeleteState(), dataItemBean.getDeleteTime()
                            , dataItemBean.getInspectionName(), dataItemBean.getInspectionType(), dataItemBean.getQuantityLowlimit(),
                            dataItemBean.getQuantityUplimit(), dataItemBean.getQuantityUnit(), dataItemBean.getValue());
                    dataItemValueList.add(new UploadDataItemValueListBean(dataItemValueListBean.getDataItemValueId(), uploadDataItemBean.getValue()));
                }
                uploadDataListBeen.add(new UploadDataListBean(taskEquipmentBean.getDataList().get(j).getDataId(), dataItemValueList));
            }
            uploadEquipList.add(new UploadTaskEquipmentBean(taskEquipmentBean.getTaskEquipmentId(), taskEquipmentBean.getTaskEquipmentState()
                    , new UploadEquipmentBean(taskEquipmentBean.getEquipment().getDeleteState(), taskEquipmentBean.getEquipment().getEquipmentId()
                    , taskEquipmentBean.getEquipment().getEquipmentName(), taskEquipmentBean.getEquipment().getEquipmentNumber()
                    , taskEquipmentBean.getEquipment().getEquipmentRemark(), taskEquipmentBean.getEquipment().getManufactureTime()
                    , taskEquipmentBean.getEquipment().getManufacturer(), taskEquipmentBean.getEquipment().getSupplier()), uploadDataListBeen));
        }
        List<UploadRoomListBean> uploadRoomList = new ArrayList<>();
        uploadRoomList.add(new UploadRoomListBean(roomDataList.getRoom(), roomDataList.getStartTime(), roomDataList.getTaskRoomId()
                , roomDataList.getTaskRoomState(), roomDataList.getEndTime(), uploadEquipList));
        UploadTaskInfo uploadTaskInfo = new UploadTaskInfo(detailBean.getEndTime(), detailBean.getIsManualCreated(), detailBean.getPlanEndTime(), detailBean.getPlanStartTime()
                , detailBean.getStartTime(), detailBean.getTaskId(), detailBean.getTaskName(), detailBean.getTaskState(), uploadRoomList);
        String json = new Gson().toJson(new UploadInspectionBean(uploadTaskInfo));
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.UploadInspection.class)
                .uploadInspection(json);
        return new ApiCallBack<String>(observable) {

            @Override
            public void onSuccess(String strings) {
                Observable.just(needUploadEquip)
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Action1<List<TaskEquipmentBean>>() {
                            @Override
                            public void call(List<TaskEquipmentBean> taskEquipmentBeen) {
                                List<EquipmentDb> equipmentDbs = new ArrayList<>();
                                for (int i = 0; i < needUploadEquip.size(); i++) {
                                    needUploadEquip.get(i).getEquipment().getEquipmentDb().setUploadState(true);
                                    equipmentDbs.add(needUploadEquip.get(i).getEquipment().getEquipmentDb());
                                }
                                InspectionApp.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(equipmentDbs);
                                long uploadCount = InspectionApp.getInstance().getDaoSession().getEquipmentDbDao().queryBuilder()
                                        .where(EquipmentDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                                , EquipmentDbDao.Properties.TaskId.eq(detailBean.getTaskId())
                                                , EquipmentDbDao.Properties.RoomId.eq(roomDataList.getTaskRoomId())
                                                , EquipmentDbDao.Properties.UploadState.eq(true)).count();
                                roomDataList.getRoomDb().setCheckCount((int) uploadCount);
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<TaskEquipmentBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                callBack.onError();
                            }

                            @Override
                            public void onNext(List<TaskEquipmentBean> taskEquipmentBeen) {
                                callBack.onSuccess();
                            }
                        });
            }

            @Override
            public void onFail() {
                callBack.onError();
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription getInspectionDetailList(@NonNull String taskId, @NonNull final IObjectCallBack<InspectionDetailBean> callBack) {
        Observable<Bean<InspectionDetailBean>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getInspectionDetailList(taskId);
        return new ApiCallBack<InspectionDetailBean>(observable) {
            @Override
            public void onSuccess(final InspectionDetailBean data) {
                if (data.getRoomList() != null) {
                    final long taskId = data.getTaskId();
                    Observable.just(data)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext(new Action1<InspectionDetailBean>() {

                                @Override
                                public void call(InspectionDetailBean inspectionDetailBean) {
                                    List<RoomDb> needSaveRoomDbs = new ArrayList<>();
                                    for (int i = 0; i < inspectionDetailBean.getRoomList().size(); i++) {
                                        RoomDb roomDb = InspectionApp.getInstance().getDaoSession()
                                                .getRoomDbDao()
                                                .queryBuilder()
                                                .where(RoomDbDao.Properties.TaskId.eq(taskId)
                                                        , RoomDbDao.Properties.UserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                                        , RoomDbDao.Properties.RoomId.eq(inspectionDetailBean.getRoomList().get(i).getTaskRoomId()))
                                                .unique();
                                        if (roomDb == null) {
                                            roomDb = new RoomDb();
                                            roomDb.setRoomId(inspectionDetailBean.getRoomList().get(i).getTaskRoomId());
                                            roomDb.setTaskId(taskId);
                                            roomDb.setCheckCount(0);
                                            roomDb.setRoomId(inspectionDetailBean.getRoomList().get(i).getTaskRoomId());
                                            if (roomDb.getTakePhotoPosition() == -1) {
                                                int randomValue = (int) (Math.random() * inspectionDetailBean.getRoomList().get(i).getTaskEquipment().size());
                                                roomDb.setTakePhotoPosition(inspectionDetailBean.getRoomList().get(i).getTaskEquipment().get(randomValue).getTaskEquipmentId());
                                            }
                                            needSaveRoomDbs.add(roomDb);
                                        }
                                        inspectionDetailBean.getRoomList().get(i).setRoomDb(roomDb);
                                    }
                                    if (needSaveRoomDbs.size() > 0) {
                                        InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(needSaveRoomDbs);
                                    }
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<InspectionDetailBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    callBack.onFinish();
                                    callBack.onError(e.getMessage());
                                }

                                @Override
                                public void onNext(InspectionDetailBean inspectionDetailBean) {
                                    callBack.onFinish();
                                    callBack.onSuccess(inspectionDetailBean);
                                }
                            });
                } else {
                    callBack.onFinish();
                    callBack.onError("");
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
    public Subscription getInspectionData(@NonNull Long taskId, @NonNull final IObjectCallBack<InspectionDataBean> callBack) {
        Observable<Bean<InspectionDataBean>> observable = Api.createRetrofit().create(Api.Inspection.class).getInspectionData(taskId);
        return new ApiCallBack<InspectionDataBean>(observable) {
            @Override
            public void onSuccess(@Nullable InspectionDataBean dataBean) {
                callBack.onFinish();
                if (dataBean != null && dataBean.getRoomList() != null && dataBean.getRoomList().size() > 0) {
                    callBack.onSuccess(dataBean);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public boolean canUploadAllData(List<TaskEquipmentBean> taskEquipmentBeans) {
        for (int i = 0; i < taskEquipmentBeans.size(); i++) {
            if (!taskEquipmentBeans.get(i).getEquipment().getEquipmentDb().getCanUpload()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean roomUploadState(long taskId, long roomId) {
        return sp.getBoolean(InspectionApp.getInstance().getCurrentUser().getUserId()
                + "_" + taskId + "_" + roomId + "_" + ConstantStr.ROOM_STATE, false);
    }


    private List<DataItemBean> needUploadPhotoItem = new ArrayList<>();//需要上传离线照片
    private IUploadOfflineCallBack uploadOfflineCallBack;//上传离线照片回调
    private int currentUploadPosition;//当前上传图片的位置

    @Override
    public boolean checkPhotoInspectionData(List<TaskEquipmentBean> taskEquipmentBeans) {
        if (needUploadPhotoItem.size() > 0) {
            needUploadPhotoItem.clear();
        }
        for (int i = 0; i < taskEquipmentBeans.size(); i++) {
            for (int j = 0; j < taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().size(); j++) {
                if (!TextUtils.isEmpty(taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().get(j).getDataItem().getLocalFile())
                        && taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().get(j)
                        .getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3
                        && TextUtils.isEmpty(taskEquipmentBeans.get(i)
                        .getDataList().get(0).getDataItemValueList().get(j).getDataItem().getValue())) {
                    needUploadPhotoItem.add(taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().get(j).getDataItem());
                }
            }
        }
        return needUploadPhotoItem.size() == 0;
    }


    @Override
    public void uploadPhotoList(final RoomDb roomDb, IUploadOfflineCallBack callBack) {
        this.uploadOfflineCallBack = callBack;
        if (TextUtils.isEmpty(roomDb.getUploadPhotoUrl())) {
            uploadEquipmentPhoto(roomDb.getPhotoUrl(), new IUploadPhotoCallBack() {
                @Override
                public void onSuccess(String url) {
                    roomDb.setUploadPhotoUrl(url);
                    InspectionApp.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
                    startPhotoOfflineUpload();
                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onFail() {
                    if (uploadOfflineCallBack != null) {
                        uploadOfflineCallBack.onFail();
                    }
                }
            });
        } else {
            startPhotoOfflineUpload();
        }
    }

    @Override
    public Subscription saveInputData(TaskEquipmentBean taskEquipmentBean, boolean isAuto) {
        if (!isAuto) {
            taskEquipmentBean.getEquipment().getEquipmentDb().setCanUpload(true);
            InspectionApp.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(taskEquipmentBean.getEquipment().getEquipmentDb());
        }
        List<EquipmentDataDb> equipmentDataDbList = new ArrayList<>();
        for (int j = 0; j < taskEquipmentBean.getDataList().get(0).getDataItemValueList().size(); j++) {
            if (taskEquipmentBean.getDataList().get(0)
                    .getDataItemValueList().get(j).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_2
                    || taskEquipmentBean.getDataList().get(0)
                    .getDataItemValueList().get(j).getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_4) {
                equipmentDataDbList.add(taskEquipmentBean.getDataList()
                        .get(0).getDataItemValueList().get(j).getDataItem().getEquipmentDataDb());
            }
        }
        return InspectionApp.getInstance().getDaoSession()
                .getEquipmentDataDbDao()
                .rx()
                .insertOrReplaceInTx(equipmentDataDbList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<Iterable<EquipmentDataDb>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Iterable<EquipmentDataDb> equipmentDataDbs) {
                    }
                });

    }

    private void startPhotoOfflineUpload() {
        currentUploadPosition = 0;
        if (needUploadPhotoItem.size() > 0) {
            uploadOfflinePhoto(needUploadPhotoItem.get(currentUploadPosition));
        } else {
            if (uploadOfflineCallBack != null) {
                uploadOfflineCallBack.onFinish();
            }
        }
    }

    private void uploadOfflinePhoto(DataItemBean dataItemBean) {
        uploadInspectionPhoto(dataItemBean, new IUploadPhotoCallBack() {
            @Override
            public void onSuccess(String url) {

            }

            @Override
            public void onFinish() {
                if (uploadOfflineCallBack != null) {
                    uploadOfflineCallBack.onFinish();
                }
            }

            @Override
            public void onFail() {
                if (uploadOfflineCallBack != null) {
                    uploadOfflineCallBack.onFail();
                }
            }
        });
    }

    /**
     * 上传巡检拍照设备
     *
     * @param localPhoto 地址
     * @param callBack   回调
     */
    private void uploadEquipmentPhoto(String localPhoto, final IUploadPhotoCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "task")
                .addFormDataPart("fileType", "image");
        File file = new File(localPhoto);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    callBack.onSuccess(strings.get(0));
                } else {
                    callBack.onFail();
                }
            }

            @Override
            public void onFail() {
                callBack.onFail();

            }
        }.execute().subscribe();
    }

    /**
     * 上传巡检拍照数据
     *
     * @param dataItemBean 录入的数据
     * @param callBack     回调
     */
    private void uploadInspectionPhoto(final DataItemBean dataItemBean, final IUploadPhotoCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "task")
                .addFormDataPart("fileType", "image");
        File file = new File(dataItemBean.getLocalFile());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    callBack.onSuccess(strings.get(0));
                    dataItemBean.setValue(strings.get(0));
                    dataItemBean.getEquipmentDataDb().setValue(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplace(dataItemBean.getEquipmentDataDb());
                    ++currentUploadPosition;
                    if (needUploadPhotoItem.size() > currentUploadPosition) {
                        uploadOfflinePhoto(needUploadPhotoItem.get(currentUploadPosition));
                    } else {
                        callBack.onFinish();
                    }
                } else {
                    callBack.onFail();
                }
            }

            @Override
            public void onFail() {
                callBack.onFail();

            }
        }.execute().subscribe();
    }


}
