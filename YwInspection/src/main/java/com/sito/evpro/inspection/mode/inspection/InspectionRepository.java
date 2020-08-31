package com.sito.evpro.inspection.mode.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.api.ApiCallBack;
import com.sito.evpro.inspection.api.ApiCallBack1;
import com.sito.evpro.inspection.api.HTTPManager;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.EmergencyCall;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.ImageDao;
import com.sito.evpro.inspection.mode.bean.db.UserInfo;
import com.sito.evpro.inspection.mode.bean.db.Voice;
import com.sito.evpro.inspection.mode.bean.db.VoiceDao;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.equip.EquipRoom;
import com.sito.evpro.inspection.mode.bean.equip.EquipType;
import com.sito.evpro.inspection.mode.bean.fault.CheckBean;
import com.sito.evpro.inspection.mode.bean.fault.FaultList;
import com.sito.evpro.inspection.mode.bean.greasing.InjectEquipment;
import com.sito.evpro.inspection.mode.bean.greasing.InjectResultBean;
import com.sito.evpro.inspection.mode.bean.greasing.InjectRoomBean;
import com.sito.evpro.inspection.mode.bean.increment.IncreList;
import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionBean;
import com.sito.evpro.inspection.mode.bean.inspection.OperationBean;
import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.evpro.inspection.mode.bean.overhaul.RepairWorkBean;
import com.sito.evpro.inspection.mode.bean.overhaul.WorkBean;
import com.sito.library.utils.Base64Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡检版
 * Created by zhangan on 2017-06-22.
 */
@Singleton
public class InspectionRepository implements InspectionDataSource {

    private SharedPreferences sp;
    private SharedPreferences userSP;
    private long WELCOME_TIME = 1500;

    @Inject
    public InspectionRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
        userSP = context.getSharedPreferences(ConstantStr.USER_INFO, Context.MODE_PRIVATE);
    }

    @Override
    public void ignoreVersion(NewVersion version) {

    }

    /**
     * 更新用户密码
     *
     * @param oldPassWord 老密码
     * @param newPassWord 新密码
     * @param callBack    回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription updateUserPassWord(String oldPassWord, String newPassWord, @NonNull final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userPwd", oldPassWord);
            jsonObject.put("userPwdNew", newPassWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable1 = Api.createRetrofit().create(Api.UserUpdate.class).updateUserInfo(jsonObject.toString());
        return new ApiCallBack<String>(observable1) {
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
        }.execute().subscribe();
    }

    /**
     * 提交反馈
     *
     * @param title    标题
     * @param content  内容
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription postQuestion(String title, String content, @NonNull final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("feedbackTitle", title);
            jsonObject.put("feedbackText", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Suggest.class).postSuggest(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String s) {
                callBack.onFinish();
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取紧急电话
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getEmergencyCalls(final IListCallBack<EmergencyCall> callBack) {
        Observable<Bean<List<EmergencyCall>>> observable = Api.createRetrofit().create(Api.GetEmergency.class).getEmergencyCalls();
        return new ApiCallBack<List<EmergencyCall>>(observable) {
            @Override
            public void onSuccess(List<EmergencyCall> emergencyCalls) {
                callBack.onFinish();
                if (emergencyCalls != null && emergencyCalls.size() > 0) {
                    callBack.onSuccess(emergencyCalls);
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

    /**
     * 获取新版本
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getNewVersion(@NonNull final NewVersionCallBack callBack) {
        Observable<Bean<NewVersion>> observable = Api.createRetrofit().create(Api.NewVersionApi.class).newVersion();
        return new ApiCallBack<NewVersion>(observable) {
            @Override
            public void onSuccess(@Nullable NewVersion result) {
                if (result != null && result.getVersion() > ConstantStr.VERSION_NO) {
                    callBack.newVersion(result);
                } else {
                    callBack.noNewVersion();
                }
            }

            @Override
            public void onFail() {
                callBack.noNewVersion();
            }
        }.execute().subscribe();
    }

    /**
     * 登陆
     *
     * @param name     用户名
     * @param pass     用户密码
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription login(@NonNull final String name, @NonNull final String pass, @NonNull final LoadUserCallBack callBack) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", name);
            jsonObject.put("userPwd", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<User>> observable = Api.createRetrofit().create(Api.Login.class)
                .userLogin(jsonObject.toString());
        return new HTTPManager<>(observable).subscribe(new ApiCallBack1<User>() {
            @Override
            public void onSuccess(User result) {
                saveUserInfo(name, pass);
                InspectionApp.getInstance().setCurrentUser(result);
                try {
                    String encryptionStr = Base64Util.encode(jsonObject.toString().getBytes("UTF-8"));
                    userSP.edit().putString(ConstantStr.USER_INFO, encryptionStr).apply();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                UserInfo info = new UserInfo();
                info.setUserId((long) result.getUserId());
                InspectionApp.getInstance().getDaoSession().getUserInfoDao().insertOrReplace(info);
                Observable<Bean<List<OptionBean>>> observable1 = Api.createRetrofit().create(Api.Option.class)
                        .getOptionInfo();
                new ApiCallBack<List<OptionBean>>(observable1) {
                    @Override
                    public void onSuccess(List<OptionBean> optionBeen) {
                        InspectionApp.getInstance().setOptionInfo(optionBeen);
                        Map<String, Map<String, String>> mOption = new HashMap<>();
                        for (int i = 0; i < optionBeen.size(); i++) {
                            Map<String, String> map = new HashMap<>();
                            String optionId = optionBeen.get(i).getOptionId() + "";
                            for (int j = 0; j < optionBeen.get(i).getItemList().size(); j++) {
                                String itemCode = optionBeen.get(i).getItemList().get(j).getItemCode();
                                String itemName = optionBeen.get(i).getItemList().get(j).getItemName();
                                map.put(itemCode, itemName);
                            }
                            mOption.put(optionId, map);
                        }
                        InspectionApp.getInstance().setMapOption(mOption);
                        callBack.onFinish();
                        callBack.onLoginSuccess(InspectionApp.getInstance().getCurrentUser());
                        //友盟账号的统计
                        MobclickAgent.onProfileSignIn(String.valueOf(InspectionApp.getInstance().getCurrentUser().getUserId()));
                    }

                    @Override
                    public void onFail() {
                        callBack.onFinish();
                        callBack.onLoginFail();
                    }
                }.execute().subscribe();
            }

            @Override
            public void onError(String message) {
                callBack.onFinish();
                callBack.onLoginFail();
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param name 用户名
     * @param pass 密码
     */
    private void saveUserInfo(@NonNull String name, @NonNull String pass) {
        name = Base64Util.encode(name.getBytes());
        pass = Base64Util.encode(pass.getBytes());
        userSP.edit().putString(ConstantStr.USER_NAME, name).apply();
        userSP.edit().putString(ConstantStr.USER_PASS, pass).apply();
    }

    /**
     * 自动登陆
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription autoLogin(@NonNull final SplashCallBack callBack) {
        String userInfo = userSP.getString(ConstantStr.USER_INFO, null);
        if (TextUtils.isEmpty(userInfo)) {
            return Observable.just(null).delaySubscription(WELCOME_TIME, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            callBack.onNeedLogin();
                        }
                    });
        } else {
            String decryptStr = "";
            try {
                decryptStr = new String(Base64Util.decode(userInfo), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Observable<Bean<User>> observable = Api.createRetrofit().create(Api.Login.class)
                    .userLogin(decryptStr);
            final long startTime = System.currentTimeMillis();
            return observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            InspectionApp.getInstance().showToast(throwable.getMessage());
                        }
                    })
                    .doOnNext(new Action1<Bean<User>>() {
                        @Override
                        public void call(Bean<User> userBean) {
                            if (userBean.getErrorCode() == 0) {
                                InspectionApp.getInstance().setCurrentUser(userBean.getData());
                            } else {
                                InspectionApp.getInstance().showToast(userBean.getMessage());
                                callBack.onNeedLogin();
                            }
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<Bean<User>, Observable<Bean<List<OptionBean>>>>() {
                        @Override
                        public Observable<Bean<List<OptionBean>>> call(Bean<User> userBean) {
                            if (userBean.getErrorCode() == 0) {
                                return Api.createRetrofit().create(Api.Option.class)
                                        .getOptionInfo();
                            }
                            return Observable.just(null);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Bean<List<OptionBean>>>() {

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            callBack.onNeedLogin();
                            InspectionApp.getInstance().showToast(e.getMessage());
                        }

                        @Override
                        public void onNext(Bean<List<OptionBean>> listBean) {
                            if (listBean != null && listBean.getErrorCode() == 0) {
                                InspectionApp.getInstance().setOptionInfo(listBean.getData());
                                Map<String, Map<String, String>> mOption = new HashMap<>();
                                for (int i = 0; i < listBean.getData().size(); i++) {
                                    Map<String, String> map = new HashMap<>();
                                    String optionId = listBean.getData().get(i).getOptionId() + "";
                                    for (int j = 0; j < listBean.getData().get(i).getItemList().size(); j++) {
                                        String itemCode = listBean.getData().get(i).getItemList().get(j).getItemCode();
                                        String itemName = listBean.getData().get(i).getItemList().get(j).getItemName();
                                        map.put(itemCode, itemName);
                                    }
                                    mOption.put(optionId, map);
                                }
                                InspectionApp.getInstance().setMapOption(mOption);
                                autoFinish(startTime, callBack);
                            } else {
                                if (listBean != null) {
                                    InspectionApp.getInstance().showToast(listBean.getMessage());
                                }
                                callBack.onNeedLogin();
                            }
                        }
                    });
        }
    }

    /**
     * 自动登陆完成
     *
     * @param startTime 开始时间
     * @param callBack  回调
     */
    private void autoFinish(long startTime, @NonNull final SplashCallBack callBack) {
        long finishTime = System.currentTimeMillis();
        if (finishTime - startTime < WELCOME_TIME) {
            long waiteTime = WELCOME_TIME - (finishTime - startTime);
            Observable.just(null).delaySubscription(waiteTime, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            callBack.onAutoFinish();
                        }
                    });
        } else {
            callBack.onAutoFinish();
        }
    }

    /**
     * 获取巡检列表
     *
     * @param data     日期
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getInspectionList(String data, @NonNull final IListCallBack<InspectionBean> callBack) {
        Observable<Bean<List<InspectionBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getInspectionList(ConstantInt.INSPECTION_TYPE, data, ConstantInt.MAX_PAGE_SIZE);
        return new ApiCallBack<List<InspectionBean>>(observable) {

            @Override
            public void onSuccess(List<InspectionBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen == null || inspectionBeen.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 领取任务
     *
     * @param taskId   任务id
     * @param position 位置
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription operationTask(String taskId, final int position, final IObjectCallBack<OperationBean> callBack) {
        Observable<Bean<OperationBean>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .operationTask(taskId, ConstantInt.OPERATION_STATE_1);
        return new ApiCallBack<OperationBean>(observable) {
            @Override
            public void onSuccess(OperationBean operationBean) {
                OperationBean bean = new OperationBean();
                callBack.onSuccess(bean);
            }

            @Override
            public void onFail() {
                InspectionApp.getInstance().showToast("领取失败");
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取巡检列表
     *
     * @param data     日期
     * @param lastId   列表最后数据id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getInspectionList(String data, String lastId, final IListCallBack<InspectionBean> callBack) {
        Observable<Bean<List<InspectionBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getInspectionList(ConstantInt.INSPECTION_TYPE, data, ConstantInt.PAGE_SIZE, lastId);
        return new ApiCallBack<List<InspectionBean>>(observable) {

            @Override
            public void onSuccess(List<InspectionBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen.size() < ConstantInt.PAGE_SIZE) {
                    callBack.onSuccess(inspectionBeen);
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取设备信息
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getEquipInfo(@NonNull final IListCallBack<EquipBean> callBack) {
        Observable<Bean<List<EquipBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipInfo();
        return new ApiCallBack<List<EquipBean>>(observable) {
            @Override
            public void onSuccess(List<EquipBean> equipBeen) {
                callBack.onFinish();
                if (equipBeen != null && equipBeen.size() > 0) {
                    callBack.onSuccess(equipBeen);
                    InspectionApp.getInstance().setEquipInfo(equipBeen);
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

    /**
     * 获取字典数据
     *
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getOptionInfo(@NonNull final IListCallBack<OptionBean> callBack) {
        Observable<Bean<List<OptionBean>>> observable = Api.createRetrofit().create(Api.Option.class)
                .getOptionInfo();
        return new ApiCallBack<List<OptionBean>>(observable) {
            @Override
            public void onSuccess(List<OptionBean> optionBeen) {
                callBack.onSuccess(optionBeen);
                InspectionApp.getInstance().setOptionInfo(optionBeen);
                Map<String, Map<String, String>> mOption = new HashMap<>();
                for (int i = 0; i < optionBeen.size(); i++) {
                    Map<String, String> map = new HashMap<>();
                    String optionId = optionBeen.get(i).getOptionId() + "";
                    for (int j = 0; j < optionBeen.get(i).getItemList().size(); j++) {
                        String itemCode = optionBeen.get(i).getItemList().get(j).getItemCode();
                        String itemName = optionBeen.get(i).getItemList().get(j).getItemName();
                        map.put(itemCode, itemName);
                    }
                    mOption.put(optionId, map);
                }
                InspectionApp.getInstance().setMapOption(mOption);
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取检修列表
     *
     * @param data     日期
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getOverhaulList(String data, @NonNull final IListCallBack<OverhaulBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", data);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", ConstantInt.MAX_PAGE_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<OverhaulBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getOverhaulList(jsonObject.toString());
        return new ApiCallBack<List<OverhaulBean>>(observable) {

            @Override
            public void onSuccess(List<OverhaulBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen == null || inspectionBeen.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取检修列表
     *
     * @param data     日期
     * @param lastId   检修列表最后id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getOverhaulList(String data, String lastId, @NonNull final IListCallBack<OverhaulBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", data);
            jsonObject.put("agentType", 0);
            jsonObject.put("count", ConstantInt.PAGE_SIZE);
            jsonObject.put("lastId", lastId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<OverhaulBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getOverhaulList(jsonObject.toString());
        return new ApiCallBack<List<OverhaulBean>>(observable) {

            @Override
            public void onSuccess(List<OverhaulBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen.size() < ConstantInt.PAGE_SIZE) {
                    callBack.onSuccess(inspectionBeen);
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取增值工作列表
     *
     * @param data     日期
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getIncrementList(String data, @NonNull final IListCallBack<IncrementBean> callBack) {
        Observable<Bean<List<IncrementBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getIncrementList(0, data, ConstantInt.MAX_PAGE_SIZE);
        return new ApiCallBack<List<IncrementBean>>(observable) {

            @Override
            public void onSuccess(List<IncrementBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen == null || inspectionBeen.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取增值列表
     *
     * @param data     日期
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getIncrementList(String data, String lastId, @NonNull final IListCallBack<IncrementBean> callBack) {
        Observable<Bean<List<IncrementBean>>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getIncrementList(data, ConstantInt.PAGE_SIZE, lastId);
        return new ApiCallBack<List<IncrementBean>>(observable) {

            @Override
            public void onSuccess(List<IncrementBean> inspectionBeen) {
                callBack.onFinish();
                if (inspectionBeen.size() < ConstantInt.PAGE_SIZE) {
                    callBack.onSuccess(inspectionBeen);
                    callBack.onError("");
                } else {
                    callBack.onSuccess(inspectionBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取政治工作历史列表
     *
     * @param count    数量
     * @param lastId   最后的id
     * @param callBack 回调
     * @return 订阅
     */
    @Override
    public Subscription getIncrementMoreHistoryList(int count, long lastId, @NonNull final IListCallBack<IncrementBean> callBack) {
        Observable<Bean<List<IncrementBean>>> observable = Api.createRetrofit().create(Api.HistoryIncre.class)
                .getMoreIncreList(lastId, count, "0");
        return new ApiCallBack<List<IncrementBean>>(observable) {
            @Override
            public void onSuccess(List<IncrementBean> list) {
                callBack.onFinish();
                if (list.size() < ConstantInt.PAGE_SIZE) {
                    callBack.onSuccess(list);
                    callBack.onError("");
                } else {
                    callBack.onSuccess(list);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 获取检修工作
     *
     * @param repairId 维修id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription getRepairWork(@NonNull String repairId, @NonNull final IObjectCallBack<OverhaulBean> callBack) {
        Observable<Bean<OverhaulBean>> observable = Api.createRetrofit().create(Api.Inspection.class)
                .getRepairWork(repairId);
        return new ApiCallBack<OverhaulBean>(observable) {
            @Override
            public void onSuccess(OverhaulBean data) {
                callBack.onFinish();
                callBack.onSuccess(data);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    /**
     * 从数据库获取检修工作
     *
     * @param repairId 维修id
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription loadRepairWorkFromDb(@NonNull final String repairId, @NonNull final IObjectCallBack<WorkBean> callBack) {
        return Observable.just(new WorkBean())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<WorkBean, Observable<WorkBean>>() {
                    @Override
                    public Observable<WorkBean> call(WorkBean workBean) {
                        List<Image> images = InspectionApp.getInstance().getDaoSession().getImageDao()
                                .queryBuilder().where(ImageDao.Properties.CurrentUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                        , ImageDao.Properties.WorkType.eq(ConstantInt.CHECKPAIR)
                                        , ImageDao.Properties.ItemId.eq(Long.valueOf(repairId)))
                                .list();
                        if (images != null) {
                            workBean.setImages(images);
                        }
                        try {
                            Voice voice = InspectionApp.getInstance().getDaoSession().getVoiceDao()
                                    .queryBuilder().where(VoiceDao.Properties.CurrentUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
                                            , VoiceDao.Properties.ItemId.eq(Long.valueOf(repairId))
                                            , VoiceDao.Properties.WorkType.eq(ConstantInt.CHECKPAIR))
                                    .unique();
                            if (voice != null) {
                                workBean.setVoice(voice);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return Observable.just(workBean);
                    }
                })
                .subscribe(new Subscriber<WorkBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFinish();
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(WorkBean workBean) {
                        callBack.onFinish();
                        callBack.onSuccess(workBean);
                    }
                });
    }

    /**
     * 上传检修数据
     *
     * @param jsonObject 工作对象
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    @Override
    public Subscription uploadOverhaulData(@NonNull JSONObject jsonObject, @NonNull final UploadRepairDataCallBack callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Inspection.class).uploadRepairWork(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String s) {
                callBack.uploadSuccess();
            }

            @Override
            public void onFail() {
                callBack.uploadFail();
            }
        }.execute().subscribe();
    }


    @Override
    public Subscription getEquipType(@NonNull final IListCallBack<EquipType> callBack) {
        Observable<Bean<List<EquipType>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipType();
        return new ApiCallBack<List<EquipType>>(observable) {
            @Override
            public void onSuccess(List<EquipType> equipTypes) {
                callBack.onFinish();
                callBack.onSuccess(equipTypes);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public Subscription getEquipRoom(@NonNull final IListCallBack<EquipRoom> callBack) {
        Observable<Bean<List<EquipRoom>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipPlace();
        return new ApiCallBack<List<EquipRoom>>(observable) {
            @Override
            public void onSuccess(List<EquipRoom> equipRooms) {
                callBack.onFinish();
                callBack.onSuccess(equipRooms);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public Subscription getEquipList(@NonNull Map<String, Object> map, final IListCallBack<EquipmentBean> callBack) {
        Observable<Bean<List<EquipmentBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipList(map);
        return new ApiCallBack<List<EquipmentBean>>(observable) {
            @Override
            public void onSuccess(List<EquipmentBean> equipmentBeen) {
                callBack.onFinish();
                callBack.onSuccess(equipmentBeen);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public Subscription getEquipByTaskId(long taskId, @NonNull final IListCallBack<EquipBean> callBack) {
        Observable<Bean<List<EquipBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipByTaskId(taskId);
        return new ApiCallBack<List<EquipBean>>(observable) {
            @Override
            public void onSuccess(List<EquipBean> list) {
                callBack.onFinish();
                callBack.onSuccess(list);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public Subscription getCheckInfo(long taskId, final IObjectCallBack<CheckBean> callBack) {
        Observable<Bean<CheckBean>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
                .getCheckInfo(taskId);
        return new ApiCallBack<CheckBean>(observable) {
            @Override
            public void onSuccess(@Nullable CheckBean checkBean) {
                callBack.onFinish();
                if (checkBean != null) {
                    callBack.onSuccess(checkBean);
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
    public Subscription getFaultList(long taskId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
                .getFaultList(taskId, ConstantInt.PAGE_SIZE);
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
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
    public Subscription getMoreFaultList(long taskId, int lastId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
                .getMoreFaultList(ConstantInt.PAGE_SIZE, taskId, lastId);
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
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
    public Subscription getHistoryList(int count, @NonNull final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.HistoryFault.class)
                .getFaultList(count, InspectionApp.getInstance().getCurrentUser().getUserId() + "", "0");
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
        }.execute().subscribe();
    }

    @Override
    public Subscription getMoreHistoryList(int count, long lastId, @NonNull final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.HistoryFault.class)
                .getMoreFaultList(lastId, count, InspectionApp.getInstance().getCurrentUser().getUserId() + "", "0");
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
        }.execute().subscribe();
    }


    @Override
    public Subscription getIncrementHistoryList(int count, @NonNull final IListCallBack<IncrementBean> callBack) {
        Observable<Bean<List<IncrementBean>>> observable = Api.createRetrofit().create(Api.HistoryIncre.class)
                .getIncreList(count, "0");
        return new ApiCallBack<List<IncrementBean>>(observable) {
            @Override
            public void onSuccess(List<IncrementBean> list) {
                callBack.onFinish();
                callBack.onSuccess(list);
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
    public Subscription getRepairDetail(@NonNull String repairId, @NonNull final IObjectCallBack<OverhaulBean> callBack) {
        Observable<Bean<OverhaulBean>> observable = Api.createRetrofit().create(Api.Inspection.class).getRepairDetail(repairId);
        return new ApiCallBack<OverhaulBean>(observable) {
            @Override
            public void onSuccess(@Nullable OverhaulBean repairWorkBean) {
                callBack.onFinish();
                if (repairWorkBean != null) {
                    callBack.onSuccess(repairWorkBean);
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
    public Subscription getSecureInfo(long securityId, @NonNull final IObjectCallBack<SecureBean> callBack) {
        Observable<Bean<SecureBean>> observable = Api.createRetrofit().create(Api.TaskInfo.class)
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
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription exitApp() {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.UserExitApp.class).exitApp();
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String s) {

            }

            @Override
            public void onFail() {

            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription uploadUserPhoto(@NonNull File file, @NonNull final IObjectCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", "user")
                .addFormDataPart("fileType", "image");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    String userPhotoUrl = strings.get(0);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("portraitUrl", userPhotoUrl);
                        InspectionApp.getInstance().getCurrentUser().setPortraitUrl(userPhotoUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Observable<Bean<String>> observable1 = Api.createRetrofit().create(Api.UserUpdate.class).updateUserInfo(jsonObject.toString());
                    new ApiCallBack<String>(observable1) {
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
                    }.execute().subscribe();
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
    public Subscription getCode(@NonNull String phoneNum, @NonNull final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.UserUpdate.class).sendCode(jsonObject.toString());
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
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription resetPwd(@NonNull String phone, @NonNull String userPwd, @NonNull String vCode, @NonNull final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("userPwd", userPwd);
            jsonObject.put("vCode", vCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.UserUpdate.class).updateUserPassWord(jsonObject.toString());
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
        }.execute().subscribe();
    }


    @NonNull
    @Override
    public Subscription uploadSoSData( @NonNull JSONObject jsonObject,  @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable1 = Api.createRetrofit().create(Api.GetEmergency.class).uploadEmergencyData(jsonObject.toString());
        return new ApiCallBack<String>(observable1) {
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
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription getOverhaulNote(@NonNull String jobId, @NonNull final IObjectCallBack<OverhaulNoteBean> callBack) {
        Observable<Bean<OverhaulNoteBean>> observable = Api.createRetrofit().create(Api.Inspection.class).getOverhaulNote(jobId);
        return new ApiCallBack<OverhaulNoteBean>(observable) {
            @Override
            public void onSuccess(OverhaulNoteBean overhaulNoteBean) {
                callBack.onFinish();
                if (overhaulNoteBean != null && overhaulNoteBean.getPageList() != null && overhaulNoteBean.getPageList().size() > 0) {
                    callBack.onSuccess(overhaulNoteBean);
                } else {
                    callBack.onError("没有数据");
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
    public Subscription getInjectRoomList(int roomType, @NonNull final IListCallBack<InjectRoomBean> callBack) {
        Observable<Bean<List<InjectRoomBean>>> observable = Api.createRetrofit().create(Api.InjectEquipmentApi.class).getInjectRoom(roomType);
        return new ApiCallBack<List<InjectRoomBean>>(observable) {
            @Override
            public void onSuccess(List<InjectRoomBean> roomBeanList) {
                callBack.onFinish();
                if (roomBeanList != null && roomBeanList.size() > 0) {
                    callBack.onSuccess(roomBeanList);
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

    @NonNull
    @Override
    public Subscription getInjectEquipmentList(long roomId, @NonNull final IListCallBack<InjectEquipment> callBack) {
        Observable<Bean<List<InjectEquipment>>> observable = Api.createRetrofit().create(Api.InjectEquipmentApi.class).getInjectEquipment(roomId);
        return new ApiCallBack<List<InjectEquipment>>(observable) {
            @Override
            public void onSuccess(List<InjectEquipment> injectEquipments) {
                callBack.onFinish();
                long sysTime = System.currentTimeMillis();
                if (injectEquipments != null && injectEquipments.size() > 0) {
                    for (int i = 0; i < injectEquipments.size(); i++) {
                        if (injectEquipments.get(i).getInjectionOil() != null) {
                            injectEquipments.get(i).setTime(sysTime - injectEquipments.get(i).getCycle() * 24L * 60L * 60L * 1000L
                                    - injectEquipments.get(i).getInjectionOil().getCreateTime());
                        } else {
                            injectEquipments.get(i).setTime(sysTime);
                        }
                    }
                    Collections.sort(injectEquipments);
                    callBack.onSuccess(injectEquipments);
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

    private long injectTime = 1700;
    private long startTime;
    private long finishTime;

    @NonNull
    @Override
    public Subscription injectEquipmentList(long equipmentId, Integer cycle, @NonNull final IObjectCallBack<InjectResultBean> callBack) {
        Observable<Bean<InjectResultBean>> observable = Api.createRetrofit().create(Api.InjectEquipmentApi.class).injectEquipment(equipmentId, cycle);
        startTime = System.currentTimeMillis();
        return new ApiCallBack<InjectResultBean>(observable) {
            @Override
            public void onSuccess(final InjectResultBean resultBean) {
                finishTime = System.currentTimeMillis();
                long waiteTime = injectTime - (finishTime - startTime);
                Observable.just(null).delaySubscription(waiteTime, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                callBack.onFinish();
                                callBack.onSuccess(resultBean);
                            }
                        });
            }

            @Override
            public void onFail() {
                finishTime = System.currentTimeMillis();
                long waiteTime = injectTime - (finishTime - startTime);
                Observable.just(null).delaySubscription(waiteTime, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                callBack.onFinish();
                                callBack.onError("");
                            }
                        });
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription startIncrement(long workId, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Inspection.class).startIncrement(workId);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String str) {
                callBack.onFinish();
                callBack.onSuccess(str);
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
    public Subscription startOverhaul(long repairId, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Inspection.class).startOverhaul(repairId);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String str) {
                callBack.onFinish();
                callBack.onSuccess(str);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public boolean getRepairState(@NonNull String repairId, @NonNull String jobId) {
        return TextUtils.isEmpty(jobId) || Long.valueOf(jobId) == 0 || sp.getBoolean(ConstantStr.OVERHAUL_NOTE_KEY + repairId, false);
    }

    @Override
    public void setRepairState(@NonNull String repairId, @NonNull String noteId) {
        sp.edit().putBoolean(ConstantStr.OVERHAUL_NOTE_KEY + repairId, true).apply();
    }

    @NonNull
    @Override
    public Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, final @NonNull Image image
            , final @NonNull UploadImageCallBack callBack) {
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
                    InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                    callBack.onError(image);
                } else {
                    image.setIsUpload(true);
                    image.setBackUrl(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getImageDao().insertOrReplaceInTx(image);
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                callBack.onFinish();
                callBack.onError(image);
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription getEquipmentDetail(long equipmentId, @NonNull final IObjectCallBack<EquipmentBean> callBack) {
        Observable<Bean<EquipmentBean>> observable = Api.createRetrofit().create(Api.Equip.class).getEquipmentDetail(equipmentId);
        return new ApiCallBack<EquipmentBean>(observable) {
            @Override
            public void onSuccess(EquipmentBean equipmentBean) {
                callBack.onFinish();
                callBack.onSuccess(equipmentBean);
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
    public Subscription uploadFile(@NonNull String filePath, @NonNull String businessType, @NonNull String fileType
            , @NonNull final UploadFileCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", fileType);
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
                if (strings != null && strings.size() > 0) {
                    callBack.uploadSuccess(strings.get(0));
                } else {
                    callBack.uploadFail();
                }
            }

            @Override
            public void onFail() {
                callBack.uploadFail();
            }
        }.execute().subscribe();
    }


}
