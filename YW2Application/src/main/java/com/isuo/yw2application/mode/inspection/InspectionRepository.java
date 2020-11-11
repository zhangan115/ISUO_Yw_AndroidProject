package com.isuo.yw2application.mode.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDbDao;
import com.isuo.yw2application.mode.bean.db.EquipmentDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDbDao;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.db.RoomDbDao;
import com.isuo.yw2application.mode.bean.db.ShareDataDb;
import com.isuo.yw2application.mode.bean.db.ShareDataDbDao;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.db.TaskDbDao;
import com.isuo.yw2application.mode.bean.employee.EmployeeBean;
import com.isuo.yw2application.mode.bean.equip.FocusBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemBean;
import com.isuo.yw2application.mode.bean.inspection.DataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.InspectionDetailBean;
import com.isuo.yw2application.mode.bean.inspection.RoomListBean;
import com.isuo.yw2application.mode.bean.inspection.SecureBean;
import com.isuo.yw2application.mode.bean.inspection.TaskEquipmentBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadDataItemBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadDataItemValueListBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadDataListBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadEquipmentBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadInspectionBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadRoomListBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadTaskEquipmentBean;
import com.isuo.yw2application.mode.bean.inspection.upload.UploadTaskInfo;
import com.sito.library.utils.CalendarUtil;

import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡检
 * Created by zhangan on 2018/3/20.
 */

public class InspectionRepository implements InspectionSourceData {

    private static InspectionRepository repository;
    private SharedPreferences sp;

    private InspectionRepository(Context context) {
        sp = context.getSharedPreferences(ConstantStr.INSPECTION_CACHE_DATA, Context.MODE_PRIVATE);
    }

    public static InspectionRepository getRepository(Context context) {
        if (repository == null) {
            repository = new InspectionRepository(context);
        }
        return repository;
    }


    @NonNull
    @Override
    public Subscription getSecureInfo(long securityId, @NonNull final IObjectCallBack<SecureBean> callBack) {
        Observable<Bean<SecureBean>> observable = Api.createRetrofit().create(InspectionApi.class)
                .getSecureInfo(securityId);
        return new ApiCallBack<SecureBean>(observable) {
            @Override
            public void onSuccess(SecureBean secureBean) {
                callBack.onFinish();
                if (secureBean != null && secureBean.getPageList() != null && secureBean.getPageList().size() > 0) {
                    callBack.onSuccess(secureBean);
                } else {
                    callBack.onError("没有数据");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("没有数据");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getInspectionDetailList(final long taskId, @NonNull final IObjectCallBack<InspectionDetailBean> callBack) {
        Observable<Bean<InspectionDetailBean>> observable = Api.createRetrofit().create(InspectionApi.class)
                .getInspectionDetailList(taskId);
        return new ApiCallBack<InspectionDetailBean>(observable) {
            @Override
            public void onSuccess(final InspectionDetailBean data) {
                if (data.getRoomList() != null) {
                    Observable.just(data)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnNext(new Action1<InspectionDetailBean>() {

                                @Override
                                public void call(InspectionDetailBean inspectionDetailBean) {
                                    List<RoomDb> needSaveRoomDbs = new ArrayList<>();
                                    for (int i = 0; i < inspectionDetailBean.getRoomList().size(); i++) {
                                        RoomDb roomDb = Yw2Application.getInstance().getDaoSession()
                                                .getRoomDbDao()
                                                .queryBuilder()
                                                .where(RoomDbDao.Properties.TaskId.eq(taskId)
                                                        , RoomDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                                                        , RoomDbDao.Properties.RoomId.eq(inspectionDetailBean.getRoomList().get(i).getRoom().getRoomId())
                                                        , RoomDbDao.Properties.TaskRoomId.eq(inspectionDetailBean.getRoomList().get(i).getTaskRoomId()))
                                                .unique();
                                        if (roomDb == null) {
                                            roomDb = new RoomDb();
                                            roomDb.setTaskId(taskId);
                                            roomDb.setTaskRoomId(inspectionDetailBean.getRoomList().get(i).getTaskRoomId());
                                            roomDb.setRoomId(inspectionDetailBean.getRoomList().get(i).getRoom().getRoomId());
                                            roomDb.setRoomName(inspectionDetailBean.getRoomList().get(i).getRoom().getRoomName());
                                            roomDb.setCheckCount(0);
                                            if (roomDb.getTakePhotoPosition() == -1) {
                                                int randomValue = (int) (Math.random() * inspectionDetailBean.getRoomList().get(i).getTaskEquipment().size());
                                                roomDb.setTakePhotoPosition(inspectionDetailBean.getRoomList().get(i).getTaskEquipment().get(randomValue).getTaskEquipmentId());
                                            }
                                        }
                                        roomDb.setTaskState(inspectionDetailBean.getRoomList().get(i).getTaskRoomState());
                                        roomDb.setStartTime(inspectionDetailBean.getRoomList().get(i).getStartTime());
                                        roomDb.setEndTime(inspectionDetailBean.getRoomList().get(i).getEndTime());
                                        roomDb.setLastSaveTime(System.currentTimeMillis());
                                        inspectionDetailBean.getRoomList().get(i).setRoomDb(roomDb);
                                        int count = getEquipmentFinishCount(taskId, inspectionDetailBean.getRoomList().get(i));
                                        if (count == 0) {
                                            RoomListBean roomListBean = inspectionDetailBean.getRoomList().get(i);
                                            for (TaskEquipmentBean equipmentBean : roomListBean.getTaskEquipment()) {
                                                boolean isFinish = true;
                                                for (DataItemValueListBean dataItemValue : equipmentBean.getDataList().get(0).getDataItemValueList()) {
                                                    if (TextUtils.isEmpty(dataItemValue.getValue())) {
                                                        isFinish = false;
                                                        break;
                                                    }
                                                }
                                                if (isFinish) {
                                                    ++count;
                                                }
                                            }
                                        }
                                        roomDb.setCheckCount(count);
                                        needSaveRoomDbs.add(roomDb);
                                    }
                                    if (needSaveRoomDbs.size() > 0) {
                                        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(needSaveRoomDbs);
                                    }
                                    saveInspectionDataToCache(inspectionDetailBean);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<InspectionDetailBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
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
        }.execute1();
    }

    private WeakReference<InspectionDetailBean> inspectionDataWf;

    @Override
    public void saveInspectionDataToCache(InspectionDetailBean data) {
        inspectionDataWf = new WeakReference<>(data);
        sp.edit().putString(ConstantStr.INSPECTION_KEY_DATA, new Gson().toJson(data)).apply();
    }

    @Nullable
    @Override
    public InspectionDetailBean getInspectionDataFromCache() {
        if (inspectionDataWf != null && inspectionDataWf.get() != null) {
            return inspectionDataWf.get();
        }
        String cache = sp.getString(ConstantStr.INSPECTION_KEY_DATA, "");
        InspectionDetailBean bean = null;
        if (!TextUtils.isEmpty(cache)) {
            bean = new Gson().fromJson(cache, InspectionDetailBean.class);
        }
        return bean;
    }

    @NonNull
    @Override
    public Subscription loadTaskUserFromDb(final long taskId, final @NonNull LoadTaskUserCallBack callBack) {
        return Yw2Application.getInstance().getDaoSession().getTaskDbDao()
                .queryBuilder()
                .where(TaskDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                        , TaskDbDao.Properties.TaskId.eq(taskId))
                .rx()
                .list()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<TaskDb>>() {
                    @Override
                    public void call(List<TaskDb> taskDbs) {
                        if (taskDbs.size() == 0) {
                            taskDbs.add(new TaskDb(taskId, Yw2Application.getInstance().getCurrentUser().getUserId()
                                    , Yw2Application.getInstance().getCurrentUser().getRealName()));
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
                        callBack.onError();
                    }

                    @Override
                    public void onNext(List<TaskDb> taskDbs) {
                        callBack.onSuccess(new ArrayList<>(taskDbs));
                    }
                });
    }

    @NonNull
    @Override
    public Subscription saveTaskUserToDb(final long taskId, @NonNull final List<EmployeeBean> employeeBeen) {
        return Yw2Application.getInstance().getDaoSession().getTaskDbDao()
                .queryBuilder()
                .where(TaskDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                        , TaskDbDao.Properties.TaskId.eq(taskId))
                .rx()
                .list()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<List<TaskDb>>() {
                    @Override
                    public void call(List<TaskDb> taskDbs) {
                        if (taskDbs != null && taskDbs.size() > 0) {
                            Yw2Application.getInstance().getDaoSession().getTaskDbDao().deleteInTx(taskDbs);
                        }
                        List<TaskDb> taskList = new ArrayList<>();
                        for (int i = 0; i < employeeBeen.size(); i++) {
                            taskList.add(new TaskDb(taskId, employeeBeen.get(i).getUser().getUserId()
                                    , employeeBeen.get(i).getUser().getRealName()));
                        }
                        Yw2Application.getInstance().getDaoSession().getTaskDbDao().insertOrReplaceInTx(taskList);
                    }
                })
                .subscribe();
    }

    @NonNull
    @Override
    public Subscription updateRoomState(long taskId, final RoomListBean roomListBean, final int operation
            , final @NonNull IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(InspectionApi.class)
                .roomStateChange(taskId, roomListBean.getTaskRoomId(), operation);
        return new ApiCallBack<String>(observable) {

            @Override
            public void onSuccess(String s) {
                if (operation == 1) {
                    roomListBean.setTaskRoomState(ConstantInt.ROOM_STATE_2);
                    roomListBean.setStartTime(System.currentTimeMillis());
                    roomListBean.getRoomDb().setStartTime(System.currentTimeMillis());
                    roomListBean.getRoomDb().setTaskState(ConstantInt.ROOM_STATE_2);
                } else {
                    roomListBean.setTaskRoomState(ConstantInt.ROOM_STATE_3);
                    roomListBean.setEndTime(System.currentTimeMillis());
                    roomListBean.getRoomDb().setEndTime(System.currentTimeMillis());
                    roomListBean.getRoomDb().setTaskState(ConstantInt.ROOM_STATE_3);
                }
                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomListBean.getRoomDb());
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
    public Subscription loadRoomDataFromDb(final long taskId, List<RoomListBean> list
            , @NonNull final LoadRoomDataCallBack callBack) {
        return Observable.just(list)
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<List<RoomListBean>>() {
                    @Override
                    public void call(List<RoomListBean> roomListBeans) {
                        for (int i = 0; i < roomListBeans.size(); i++) {
                            RoomDb roomDb = Yw2Application.getInstance().getDaoSession()
                                    .getRoomDbDao()
                                    .queryBuilder()
                                    .where(RoomDbDao.Properties.TaskId.eq(taskId)
                                            , RoomDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                                            , RoomDbDao.Properties.RoomId.eq(roomListBeans.get(i).getRoom().getRoomId())
                                            , RoomDbDao.Properties.TaskRoomId.eq(roomListBeans.get(i).getTaskRoomId()))
                                    .unique();
                            if (roomDb != null) {
                                roomListBeans.get(i).setRoomDb(roomDb);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RoomListBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<RoomListBean> roomListBeans) {
                        callBack.onSuccess(roomListBeans);
                    }
                });
    }

    @NonNull
    @Override
    public Subscription loadRoomListDataFromDb(final long taskId, @NonNull RoomListBean roomListBean
            , @NonNull final LoadRoomListDataCallBack callBack) {
        return Observable.just(roomListBean)
                .doOnNext(new Action1<RoomListBean>() {
                    @Override
                    public void call(RoomListBean roomListBean) {
                        String removeData = null;
                        if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig() != null
                                && Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size() > 0) {
                            for (int i = 0; i < Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().size(); i++) {
                                if (Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigCode().equals("removeDataDate")) {
                                    removeData = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerConfig().get(i).getConfigValue();
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
                            equipmentDb = Yw2Application.getInstance().getDaoSession().getEquipmentDbDao().queryBuilder()
                                    .where(EquipmentDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
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
                            int uploadDataCount = 0;
                            for (int j = 0; j < taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().size(); j++) {
                                DataItemValueListBean dataItemValueListBean = taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().get(j);
                                EquipmentDataDb equipmentData = Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().queryBuilder()
                                        .where(EquipmentDataDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                                                , EquipmentDataDbDao.Properties.EquipmentId.eq(equipmentId)
                                                , EquipmentDataDbDao.Properties.RoomId.eq(roomId)
                                                , EquipmentDataDbDao.Properties.TaskId.eq(taskId)
                                                , EquipmentDataDbDao.Properties.DataItemId.eq(dataItemValueListBean.getDataItemValueId())
                                                , EquipmentDataDbDao.Properties.Type.eq(dataItemValueListBean.getDataItem().getInspectionType()))
                                        .unique();
                                DataItemBean dataItem = dataItemValueListBean.getDataItem();
                                if (equipmentData == null) {
                                    equipmentData = new EquipmentDataDb();
                                    equipmentData.setRoomId(roomId);
                                    equipmentData.setTaskId(taskId);
                                    equipmentData.setEquipmentId(equipmentId);
                                    equipmentData.setDataItemId(dataItemValueListBean.getDataItemValueId());
                                    equipmentData.setType(dataItemValueListBean.getDataItem().getInspectionType());
                                    equipmentData.setIsShareValue(dataItemValueListBean.getDataItem().getIsShareValue());
                                    equipmentData.setInspectionId(dataItemValueListBean.getDataItem().getInspectionId());
                                    dataItemValueListBean.getDataItem().setEquipmentDataDb(equipmentData);
                                    String value = taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().get(j).getValue();
                                    long userId = taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().get(j).getUserId();
                                    if (!TextUtils.isEmpty(value)) {
                                        dataItem.setValue(dataItemValueListBean.getValue());
                                        equipmentData.setValue(value);
                                        equipmentData.setUserId(userId);
                                        equipmentData.setUpload(true);
                                    }
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
                                } else {
                                    if (!TextUtils.isEmpty(dataItemValueListBean.getValue())) {
                                        dataItem.setValue(dataItemValueListBean.getValue());
                                        equipmentData.setValue(dataItemValueListBean.getValue());
                                        equipmentData.setUserId(dataItemValueListBean.getUserId());
                                        equipmentData.setUpload(true);
                                    }else{
                                        if (!TextUtils.isEmpty(equipmentData.getValue())){
                                            dataItem.setValue(equipmentData.getValue());
                                        }
                                    }
                                    if (dataItemValueListBean.getDataItem().getInspectionType() == ConstantInt.DATA_VALUE_TYPE_3
                                            && !TextUtils.isEmpty(equipmentData.getLocalPhoto())) {
                                        dataItem.setLocalFile(equipmentData.getLocalPhoto());
                                    }
                                    dataItem.setEquipmentDataDb(equipmentData);
                                }
                                if (equipmentData.getIsUpload()) {
                                    ++uploadDataCount;
                                }
                                equipmentDataDbToSave.add(equipmentData);
                            }
                            if (taskEquipmentBean.get(i).getDataList().get(0).getDataItemValueList().size() == uploadDataCount) {
                                ++uploadCount;
                                equipmentDb.setUploadState(true);
                            }
                        }
                        roomListBean.getRoomDb().setCheckCount(uploadCount);
                        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomListBean.getRoomDb());
                        if (equipmentDbToSave.size() > 0) {
                            Yw2Application.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(equipmentDbToSave);
                        }
                        if (equipmentDataDbToSave.size() > 0) {
                            Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(equipmentDataDbToSave);
                        }
                        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomListBean.getRoomDb());
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
    public Subscription uploadRoomListData(int position, final @NonNull InspectionDetailBean detailBean
            , @NonNull final UploadRoomListCallBack callBack) {
        needUploadEquip = new ArrayList<>();
        int uploadCount = 0;
        final RoomListBean roomDataList = detailBean.getRoomList().get(position);
        List<TaskEquipmentBean> taskEquipmentBeans = roomDataList.getTaskEquipment();
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
                if (taskEquipmentBeans.get(i).getDataList().get(0).getDataItemValueList().get(j).getDataItem().getIsRequired() == 0) {
                    continue;
                }
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
        Observable<Bean<String>> observable = Api.createRetrofit().create(InspectionApi.class)
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
                                List<EquipmentDataDb> equipmentDataDbs = new ArrayList<>();
                                for (int i = 0; i < needUploadEquip.size(); i++) {
                                    needUploadEquip.get(i).getEquipment().getEquipmentDb().setUploadState(true);
                                    equipmentDbs.add(needUploadEquip.get(i).getEquipment().getEquipmentDb());
                                    for(DataItemValueListBean itemValueList:needUploadEquip.get(i).getDataList().get(0).getDataItemValueList()){
                                        EquipmentDataDb equipmentDataDb = itemValueList.getDataItem().getEquipmentDataDb();
                                        equipmentDataDb.setUpload(true);
                                        equipmentDataDbs.add(equipmentDataDb);
                                    }
                                }
                                Yw2Application.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(equipmentDbs);
                                Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(equipmentDataDbs);
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadTaskEquipmentListData(int position, final @NonNull InspectionDetailBean detailBean
            , TaskEquipmentBean equipmentBean, @NonNull final UploadRoomListCallBack callBack) {
        needUploadEquip = new ArrayList<>();
        int uploadCount = 0;
        final RoomListBean roomDataList = detailBean.getRoomList().get(position);
        List<TaskEquipmentBean> taskEquipmentBeans = roomDataList.getTaskEquipment();
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
        needUploadEquip.add(equipmentBean);
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
        Observable<Bean<String>> observable = Api.createRetrofit().create(InspectionApi.class)
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
                                List<EquipmentDataDb> equipmentDataDbs = new ArrayList<>();
                                for (int i = 0; i < needUploadEquip.size(); i++) {
                                    needUploadEquip.get(i).getEquipment().getEquipmentDb().setUploadState(true);
                                    equipmentDbs.add(needUploadEquip.get(i).getEquipment().getEquipmentDb());
                                    for(DataItemValueListBean itemValueList:needUploadEquip.get(i).getDataList().get(0).getDataItemValueList()){
                                        EquipmentDataDb equipmentDataDb = itemValueList.getDataItem().getEquipmentDataDb();
                                        equipmentDataDb.setUpload(true);
                                        equipmentDataDbs.add(equipmentDataDb);
                                    }
                                }
                                Yw2Application.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(equipmentDbs);
                                Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplaceInTx(equipmentDataDbs);
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadInspectionPhoto(@NonNull final DataItemBean dataItemBean
            , @NonNull final UploadPhotoCallBack callBack) {
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
                    Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplace(dataItemBean.getEquipmentDataDb());
                    if (dataItemBean.getIsShareValue() == 1) {
                        Yw2Application.getInstance().getDaoSession().getShareDataDbDao()
                                .insertOrReplace(ShareDataDb.getShareDataDb(dataItemBean.getEquipmentDataDb()));
                    }
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
                Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().insertOrReplace(dataItemBean.getEquipmentDataDb());
                callBack.onFinish();
                callBack.onFail();
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadUserPhotoInfo(long taskId, long equipmentId, @NonNull final String url
            , final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("taskId", taskId);
            jsonObject.put("equipmentId", equipmentId);
            jsonObject.put("inplacePicUrl", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(InspectionApi.class).uploadUserPhoto(jsonObject.toString());
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadRandomDataPhoto(@NonNull final RoomDb roomDb, @NonNull final UploadPhotoCallBack callBack) {
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
                    Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplace(roomDb);
                    callBack.onSuccess();
                } else {
                    callBack.onFail();
                }
            }

            @Override
            public void onFail() {
                roomDb.setUploadPhotoUrl(null);
                Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplace(roomDb);
                callBack.onFinish();
                callBack.onFail();
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription roomListFinish(final long taskId, int operation, @NonNull String userIds
            , @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(InspectionApi.class)
                .roomListFinish(taskId, operation, userIds);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String s) {
                callBack.onFinish();
                List<ShareDataDb> shareDataDbs =
                        Yw2Application.getInstance().getDaoSession().getShareDataDbDao().queryBuilder()
                                .where(ShareDataDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                                        , ShareDataDbDao.Properties.TaskId.eq(taskId))
                                .list();
                if (shareDataDbs != null && shareDataDbs.size() > 0) {
                    Yw2Application.getInstance().getDaoSession().getShareDataDbDao().deleteInTx(shareDataDbs);
                }
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
    public Subscription getCareEquipmentData(long equipmentId, final IObjectCallBack<FocusBean> callBack) {
        Observable<Bean<FocusBean>> observable = Api.createRetrofit().create(InspectionApi.class)
                .getCareData(equipmentId);
        return new ApiCallBack<FocusBean>(observable) {
            @Override
            public void onSuccess(FocusBean s) {
                callBack.onFinish();
                if (s != null) {
                    callBack.onSuccess(s);
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
    public Subscription getShareData(TaskEquipmentBean taskEquipmentBean, final IShareDataCallBack callBack) {
        return Observable.just(taskEquipmentBean)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<TaskEquipmentBean>() {
                    @Override
                    public void call(TaskEquipmentBean taskEquipmentBean) {
                        for (int i = 0; i < taskEquipmentBean.getDataList().get(0).getDataItemValueList().size(); i++) {
                            DataItemBean dataItemBean = taskEquipmentBean.getDataList().get(0).getDataItemValueList().get(i).getDataItem();
                            if (dataItemBean != null && TextUtils.isEmpty(dataItemBean.getValue()) && dataItemBean.getIsShareValue() == 1) {
                                EquipmentDataDb equipmentDataDb = dataItemBean.getEquipmentDataDb();
                                ShareDataDb shareDataDb = null;
                                try {
                                    shareDataDb = Yw2Application.getInstance().getDaoSession().getShareDataDbDao().queryBuilder()
                                            .where(ShareDataDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                                                    , ShareDataDbDao.Properties.TaskId.eq(equipmentDataDb.getTaskId())
                                                    , ShareDataDbDao.Properties.RoomId.eq(equipmentDataDb.getRoomId())
                                                    , ShareDataDbDao.Properties.InspectionId.eq(equipmentDataDb.getInspectionId())
                                                    , ShareDataDbDao.Properties.Type.eq(equipmentDataDb.getType())
                                                    , ShareDataDbDao.Properties.Value.isNotNull()
                                            ).unique();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (shareDataDb != null) {
                                    equipmentDataDb.setValue(shareDataDb.getValue());
                                    equipmentDataDb.setChooseInspectionName(shareDataDb.getChooseInspectionName());
                                    equipmentDataDb.setLocalPhoto(shareDataDb.getLocalPhoto());
                                    dataItemBean.setValue(shareDataDb.getValue());
                                    dataItemBean.setChooseInspectionName(shareDataDb.getChooseInspectionName());
                                    dataItemBean.setLocalFile(shareDataDb.getLocalPhoto());
                                }
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TaskEquipmentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.onFail();
                    }

                    @Override
                    public void onNext(TaskEquipmentBean taskEquipmentBean) {
                        callBack.onSuccess();
                    }
                });

    }

    @Override
    public Subscription getTaskEquipmentData(@NotNull final IGetTaskEquipmentCallBack callBack) {
        return Observable.just(0).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Integer, Observable<TaskEquipmentBean>>() {
                    @Override
                    public Observable<TaskEquipmentBean> call(Integer integer) {
                        TaskEquipmentBean taskEquipmentBean = null;
                        String cache = sp.getString(ConstantStr.INSPECTION_KEY_EQUIP, "");
                        if (!TextUtils.isEmpty(cache)) {
                            taskEquipmentBean = new Gson().fromJson(cache, TaskEquipmentBean.class);
                        }
                        return Observable.just(taskEquipmentBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TaskEquipmentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.noData();
                    }

                    @Override
                    public void onNext(TaskEquipmentBean taskEquipmentBean) {
                        if (taskEquipmentBean == null) {
                            callBack.noData();
                        } else {
                            callBack.onGetData(taskEquipmentBean);
                        }
                    }
                });
    }

    private WeakReference<TaskEquipmentBean> equipDataWeak;

    @Override
    public void saveTaskEquipToRepository(TaskEquipmentBean taskEquipmentBean) {
        equipDataWeak = new WeakReference<>(taskEquipmentBean);
        sp.edit().putString(ConstantStr.INSPECTION_KEY_EQUIP, new Gson().toJson(taskEquipmentBean)).apply();
    }

    @Nullable
    @Override
    public TaskEquipmentBean getTskEquipFromRepository() {
        if (equipDataWeak != null && equipDataWeak.get() != null) {
            return equipDataWeak.get();
        }
        String cache = sp.getString(ConstantStr.INSPECTION_KEY_EQUIP, "");
        TaskEquipmentBean taskEquipmentBean = null;
        if (!TextUtils.isEmpty(cache)) {
            taskEquipmentBean = new Gson().fromJson(cache, TaskEquipmentBean.class);
        }
        return taskEquipmentBean;
    }

    @Override
    public void removeTaskEquipFormCache() {
        sp.edit().remove(ConstantStr.INSPECTION_KEY_EQUIP).apply();
    }

    @Override
    public int getEquipmentFinishCount(long taskId, RoomListBean roomBean) {
        RoomDb roomDb = roomBean.getRoomDb();
        int count = 0;
        for (TaskEquipmentBean bean : roomBean.getTaskEquipment()) {
            long equipmentCount = Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().queryBuilder()
                    .where(EquipmentDataDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                            , EquipmentDataDbDao.Properties.EquipmentId.eq(bean.getTaskEquipmentId())
                            , EquipmentDataDbDao.Properties.RoomId.eq(roomBean.getTaskRoomId())
                            , EquipmentDataDbDao.Properties.TaskId.eq(taskId)
                            , EquipmentDataDbDao.Properties.IsUpload.eq(true))
                    .count();
            if (equipmentCount == bean.getDataList().get(0).getDataItemValueList().size()) {
                count++;
            }
        }
        roomDb.setCheckCount(count);
        Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
        return count;
    }

    @Override
    public long getEquipmentInputCount(long taskId, long roomId, long equipmentId) {
        List<EquipmentDataDb> list = Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao().queryBuilder()
                .where(EquipmentDataDbDao.Properties.CurrentUserId.eq(Yw2Application.getInstance().getCurrentUser().getUserId())
                        , EquipmentDataDbDao.Properties.EquipmentId.eq(equipmentId)
                        , EquipmentDataDbDao.Properties.RoomId.eq(roomId)
                        , EquipmentDataDbDao.Properties.TaskId.eq(taskId)).list();
        int count = 0;
        for (EquipmentDataDb bean:list){
            if (!TextUtils.isEmpty(bean.getValue())){
                ++count;
            }
        }
        return count;
    }


    @Override
    public void saveInputData(TaskEquipmentBean taskEquipmentBean, final boolean isAuto) {
        if (!isAuto) {
            taskEquipmentBean.getEquipment().getEquipmentDb().setCanUpload(true);
            Yw2Application.getInstance().getDaoSession().getEquipmentDbDao().insertOrReplaceInTx(taskEquipmentBean.getEquipment().getEquipmentDb());
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
        Yw2Application.getInstance().getDaoSession()
                .getEquipmentDataDbDao()
                .rx()
                .insertOrReplaceInTx(equipmentDataDbList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Iterable<EquipmentDataDb>, Observable<Iterable<ShareDataDb>>>() {
                    @Override
                    public Observable<Iterable<ShareDataDb>> call(Iterable<EquipmentDataDb> equipmentDataDbs) {
                        List<ShareDataDb> shareDataDbs = new ArrayList<>();
                        for (EquipmentDataDb eq : equipmentDataDbs) {
                            if (eq.getIsShareValue() == 1) {
                                shareDataDbs.add(ShareDataDb.getShareDataDb(eq));
                            }
                        }
                        if (!isAuto && shareDataDbs.size() > 0) {
                            return Yw2Application.getInstance().getDaoSession().getShareDataDbDao().rx().insertOrReplaceInTx(shareDataDbs);
                        } else {
                            return Observable.just(null);
                        }
                    }
                })
                .subscribe(new Subscriber<Iterable<ShareDataDb>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Iterable<ShareDataDb> shareDataDbs) {
                    }
                });
    }

    private List<DataItemBean> needUploadPhotoItem = new ArrayList<>();//需要上传离线照片
    private IUploadOfflineCallBack uploadOfflineCallBack;//上传离线照片回调
    private int currentUploadPosition;//当前上传图片的位置

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
                    Yw2Application.getInstance().getDaoSession().getEquipmentDataDbDao()
                            .insertOrReplace(dataItemBean.getEquipmentDataDb());
                    if (dataItemBean.getIsShareValue() == 1) {
                        Yw2Application.getInstance().getDaoSession().getShareDataDbDao()
                                .insertOrReplace(ShareDataDb.getShareDataDb(dataItemBean.getEquipmentDataDb()));
                    }
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
        }.execute1();
    }

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

    @Override
    public void uploadPhotoList(final RoomDb roomDb, IUploadOfflineCallBack callBack) {
        this.uploadOfflineCallBack = callBack;
        if (TextUtils.isEmpty(roomDb.getUploadPhotoUrl())) {
            uploadEquipmentPhoto(roomDb.getPhotoUrl(), new IUploadPhotoCallBack() {
                @Override
                public void onSuccess(String url) {
                    roomDb.setUploadPhotoUrl(url);
                    Yw2Application.getInstance().getDaoSession().getRoomDbDao().insertOrReplaceInTx(roomDb);
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
        }.execute1();
    }


}