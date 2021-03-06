package com.sito.customer.mode.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.igexin.sdk.PushManager;
import com.sito.customer.api.Api;
import com.sito.customer.api.ApiCallBack;
import com.sito.customer.api.FaultApi;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.mode.bean.User;
import com.sito.customer.mode.bean.check.CheckBean;
import com.sito.customer.mode.bean.check.FaultList;
import com.sito.customer.mode.bean.count.ComeCount;
import com.sito.customer.mode.bean.count.MonthCount;
import com.sito.customer.mode.bean.count.PartPersonStatistics;
import com.sito.customer.mode.bean.count.WeekCount;
import com.sito.customer.mode.bean.count.WeekList;
import com.sito.customer.mode.bean.db.NewsBeanDao;
import com.sito.customer.mode.bean.discover.DeptType;
import com.sito.customer.mode.bean.discover.FaultLevel;
import com.sito.customer.mode.bean.discover.FaultReport;
import com.sito.customer.mode.bean.discover.StandBean;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.customer.mode.bean.equip.EquipBean;
import com.sito.customer.mode.bean.equip.EquipRoom;
import com.sito.customer.mode.bean.equip.EquipType;
import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.bean.equip.FocusBean;
import com.sito.customer.mode.bean.fault.AlarmCount;
import com.sito.customer.mode.bean.fault.FaultCountBean;
import com.sito.customer.mode.bean.fault.FaultDayCountBean;
import com.sito.customer.mode.bean.fault.FaultYearCountBean;
import com.sito.customer.mode.bean.news.EnterpriseDetailBean;
import com.sito.customer.mode.bean.news.MessageListBean;
import com.sito.customer.mode.bean.db.NewsBean;
import com.sito.customer.mode.bean.option.OptionBean;
import com.sito.customer.mode.inspection.InspectionApi;
import com.sito.customer.mode.tools.ToolsApi;
import com.sito.library.utils.Base64Util;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
public class CustomerRepository implements CustomerDataSource {

    private SharedPreferences sp;
    private long WELCOME_TIME = 1500;

    private Context mContext;

    @Inject
    public CustomerRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_INFO, Context.MODE_PRIVATE);
        mContext = context;
    }

    @Override
    public void ignoreVersion(NewVersion version) {
        sp.edit().putInt(ConstantStr.cancelVersion, version.getVersion()).apply();
    }

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
        Observable<Bean<String>> observable1 = Api.createRetrofit().create(Api.UserUpdate.class).updateUser(jsonObject.toString());
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getNewVersion(@NonNull final NewVersionCallBack callBack) {
        Observable<Bean<NewVersion>> observable =
                Api.createRetrofit().create(Api.NewVersionApi.class).newVersion();
        return new ApiCallBack<NewVersion>(observable) {
            @Override
            public void onSuccess(@Nullable NewVersion result) {
                if (result != null && result.getVersion() > ConstantStr.VERSION_NO
                        && result.getVersion() != sp.getInt(ConstantStr.cancelVersion, -1)) {
                    callBack.newVersion(result);
                } else {
                    callBack.noNewVersion();
                }
            }

            @Override
            public void onFail() {
                callBack.noNewVersion();
            }
        }.execute1();
    }

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
        if (TextUtils.isEmpty(CustomerApp.getInstance().getCid())) {
            CustomerApp.getInstance().setCid(PushManager.getInstance().getClientid(mContext));
        }
        Observable<Bean<User>> observable = Api.createRetrofit().create(Api.Login.class)
                .userLogin(jsonObject.toString());
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callBack.onFinish();
                        callBack.onLoginFail();
                        CustomerApp.getInstance().showToast(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                })
                .doOnNext(new Action1<Bean<User>>() {

                    @Override
                    public void call(Bean<User> userBean) {
                        if (userBean.getErrorCode() == 0) {
                            saveUserInfo(name, pass);
                            sp.edit().putBoolean(ConstantStr.USE_APP, true).apply();
                            CustomerApp.getInstance().setCurrentUser(userBean.getData());
                            try {
                                String encryptionStr = Base64Util.encode(jsonObject.toString().getBytes("UTF-8"));
                                sp.edit().putString(ConstantStr.USER_INFO, encryptionStr).apply();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            CustomerApp.getInstance().showToast(userBean.getMessage());
                            callBack.onFinish();
                            callBack.onLoginFail();
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Bean<User>, Observable<Bean<List<OptionBean>>>>() {
                    @Override
                    public Observable<Bean<List<OptionBean>>> call(Bean<User> userBean) {
                        if (userBean.getErrorCode() == 0) {
                            return Api.createRetrofit().create(Api.Login.class).getOptionInfo();
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
                        CustomerApp.getInstance().showToast(e.getMessage());
                        callBack.onFinish();
                        callBack.onLoginFail();
                    }

                    @Override
                    public void onNext(Bean<List<OptionBean>> listBean) {
                        callBack.onFinish();
                        if (listBean == null) {
                            callBack.onLoginFail();
                            return;
                        }
                        if (listBean.getErrorCode() == 0) {
                            CustomerApp.getInstance().setOptionInfo(listBean.getData());
                            callBack.onLoginSuccess(CustomerApp.getInstance().getCurrentUser());
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
                            CustomerApp.getInstance().setMapOption(mOption);
                            //友盟账号的统计
                            MobclickAgent.onProfileSignIn(String.valueOf(CustomerApp.getInstance().getCurrentUser().getUserId()));
                        } else {
                            callBack.onLoginFail();
                        }
                    }
                });
    }

    private void saveUserInfo(@NonNull String name, @NonNull String pass) {
        name = Base64Util.encode(name.getBytes());
        pass = Base64Util.encode(pass.getBytes());
        sp.edit().putString(ConstantStr.USER_NAME, name).apply();
        sp.edit().putString(ConstantStr.USER_PASS, pass).apply();
    }

    @NonNull
    @Override
    public Subscription autoLogin(@NonNull final SplashCallBack callBack) {
        boolean showWelcome = sp.getBoolean(ConstantStr.USE_APP, false);
        if (!showWelcome) {
            callBack.showWelcome();
            return Observable.just(false).subscribe();
        }
        String userInfo = sp.getString(ConstantStr.USER_INFO, null);
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
            if (TextUtils.isEmpty(CustomerApp.getInstance().getCid())) {
                CustomerApp.getInstance().setCid(PushManager.getInstance().getClientid(mContext));
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
                            CustomerApp.getInstance().showToast(throwable.getMessage());
                        }
                    })
                    .doOnNext(new Action1<Bean<User>>() {
                        @Override
                        public void call(Bean<User> userBean) {
                            if (userBean.getErrorCode() == 0) {
                                CustomerApp.getInstance().setCurrentUser(userBean.getData());
                            } else {
                                CustomerApp.getInstance().showToast(userBean.getMessage());
                                callBack.onNeedLogin();
                            }
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<Bean<User>, Observable<Bean<List<OptionBean>>>>() {
                        @Override
                        public Observable<Bean<List<OptionBean>>> call(Bean<User> userBean) {
                            if (userBean.getErrorCode() == 0) {
                                return Api.createRetrofit().create(Api.Login.class).getOptionInfo();
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
                        }

                        @Override
                        public void onNext(Bean<List<OptionBean>> listBean) {
                            if (listBean != null && listBean.getErrorCode() == 0) {
                                CustomerApp.getInstance().setOptionInfo(listBean.getData());
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
                                CustomerApp.getInstance().setMapOption(mOption);
                                //友盟账号的统计
                                MobclickAgent.onProfileSignIn(String.valueOf(CustomerApp.getInstance().getCurrentUser().getUserId()));
                                autoFinish(startTime, callBack);
                            } else {
                                callBack.onNeedLogin();
                            }
                        }
                    });
        }
    }

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

    @NonNull
    @Override
    public Subscription getEquipInfo(boolean isFocusNow, @NonNull final IListCallBack<EquipBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("focusNow", isFocusNow ? 1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<List<EquipBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getEquipInfo(jsonObject.toString());
        return new ApiCallBack<List<EquipBean>>(observable) {

            @Override
            public void onSuccess(List<EquipBean> equipBeen) {
                callBack.onFinish();
                callBack.onSuccess(equipBeen);
            }

            @Override
            public void onFail() {

            }
        }.execute1();
    }

    @Override
    public Subscription getTodayCount(@NonNull String time, @NonNull String deptId, @NonNull final IListCallBack<ComeCount> callBack) {
        Observable<Bean<List<ComeCount>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getTodaCount(time, deptId);
        return new ApiCallBack<List<ComeCount>>(observable) {
            @Override
            public void onSuccess(List<ComeCount> comeCounts) {
                callBack.onFinish();
                callBack.onSuccess(comeCounts);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getWeekCount(@NonNull String time, @NonNull String deptId, @NonNull final IListCallBack<WeekCount> callBack) {
        Observable<Bean<List<WeekCount>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getWeekCount(time, deptId);
        return new ApiCallBack<List<WeekCount>>(observable) {
            @Override
            public void onSuccess(List<WeekCount> weekCounts) {
                callBack.onFinish();
                callBack.onSuccess(weekCounts);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");

            }
        }.execute1();
    }

    @Override
    public Subscription getWeekList(@NonNull String time, @NonNull String deptId, @NonNull final IListCallBack<WeekList> callBack) {
        Observable<Bean<List<WeekList>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getWeekList(time, deptId);
        return new ApiCallBack<List<WeekList>>(observable) {
            @Override
            public void onSuccess(List<WeekList> monthLists) {
                callBack.onFinish();
                callBack.onSuccess(monthLists);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getMonthCount(@NonNull String time, @NonNull String deptId, @NonNull final IListCallBack<MonthCount> callBack) {
        Observable<Bean<List<MonthCount>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getMonth(time, deptId);
        return new ApiCallBack<List<MonthCount>>(observable) {
            @Override
            public void onSuccess(List<MonthCount> monthCounts) {
                callBack.onFinish();
                callBack.onSuccess(monthCounts);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
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
        }.execute1();
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
        }.execute1();
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
        }.execute1();
    }

    @Override
    public Subscription getCheckInfo(long taskId, final IObjectCallBack<CheckBean> callBack) {
        Observable<Bean<CheckBean>> observable = Api.createRetrofit().create(Api.Check.class)
                .getCheckInfo(taskId);
        return new ApiCallBack<CheckBean>(observable) {
            @Override
            public void onSuccess(@Nullable CheckBean checkBean) {
                callBack.onFinish();
                callBack.onSuccess(checkBean);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getFaultList(long taskId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Check.class)
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
        }.execute1();
    }

    @Override
    public Subscription getMoreFaultList(long taskId, int lastId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Check.class)
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
        }.execute1();
    }

    @Override
    public Subscription getTodayFault(@NonNull String startTime, @NonNull String endTime, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Check.class)
                .getTodayFault(ConstantInt.PAGE_SIZE, startTime, endTime);
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getTodayFault(int faultState, Long lastId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable;
        if (lastId == null) {
            observable = Api.createRetrofit().create(Api.Check.class)
                    .getFaultList(ConstantInt.PAGE_SIZE, faultState);
        } else {
            observable = Api.createRetrofit().create(Api.Check.class)
                    .getFaultList(ConstantInt.PAGE_SIZE, faultState, lastId);
        }
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
        }.execute1();
    }

    @Override
    public Subscription getMoreTodayFault(long lastId, @NonNull String startTime, @NonNull String endTime, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Check.class)
                .getMoreTodayFault(ConstantInt.PAGE_SIZE, lastId, startTime, endTime);
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null) {
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
    public Subscription getFaultCount(@NonNull final IObjectCallBack<FaultCountBean> callBack) {
        Observable<Bean<FaultCountBean>> observable = Api.createRetrofit().create(FaultApi.class)
                .getFaultCount();
        return new ApiCallBack<FaultCountBean>(observable) {
            @Override
            public void onSuccess(@Nullable FaultCountBean faultCountBean) {
                callBack.onFinish();
                if (faultCountBean != null) {
                    callBack.onSuccess(faultCountBean);
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
    public Subscription getFaultDayCount(@NonNull String time, @NonNull final IObjectCallBack<FaultDayCountBean> callBack) {
        Observable<Bean<FaultDayCountBean>> observable = Api.createRetrofit().create(FaultApi.class)
                .getFaultDayCount(time);
        return new ApiCallBack<FaultDayCountBean>(observable) {
            @Override
            public void onSuccess(@Nullable FaultDayCountBean bean) {
                callBack.onFinish();
                if (bean != null) {
                    callBack.onSuccess(bean);
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
    public Subscription getFaultYearCount(@NonNull String time, @NonNull final IObjectCallBack<FaultYearCountBean> callBack) {
        Observable<Bean<FaultYearCountBean>> observable = Api.createRetrofit().create(FaultApi.class)
                .getFaultYearCount(time);
        return new ApiCallBack<FaultYearCountBean>(observable) {
            @Override
            public void onSuccess(@Nullable FaultYearCountBean bean) {
                callBack.onFinish();
                if (bean != null) {
                    callBack.onSuccess(bean);
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
    public Subscription getAlarmCount(@NonNull final IObjectCallBack<AlarmCount> callBack) {
        Observable<Bean<AlarmCount>> observable = Api.createRetrofit().create(FaultApi.class)
                .getAlarmCount();
        return new ApiCallBack<AlarmCount>(observable) {
            @Override
            public void onSuccess(@Nullable AlarmCount bean) {
                callBack.onFinish();
                if (bean != null) {
                    callBack.onSuccess(bean);
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

    @Override
    public Subscription getStandInfo(@NonNull final IObjectCallBack<StandBean> callBack) {
        Observable<Bean<StandBean>> observable = Api.createRetrofit().create(Api.Stand.class)
                .getStandInfo(ConstantInt.MAX_PAGE_SIZE);
        return new ApiCallBack<StandBean>(observable) {
            @Override
            public void onSuccess(@Nullable StandBean standBeen) {
                callBack.onFinish();
                if (standBeen != null && standBeen.getList() != null && standBeen.getList().size() > 0) {
                    callBack.onSuccess(standBeen);
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
    public Subscription getAlarmList(Map<String, String> map, @NonNull final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Check.class)
                .getAlarmList(map);
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription loadNewsFromDb(int newsType, @NonNull final IListCallBack<NewsBean> callBack) {
        WhereCondition whereCondition;
        if (newsType == 0) {
            whereCondition = NewsBeanDao.Properties.IsWork.eq(true);
        } else if (newsType == 1) {
            whereCondition = NewsBeanDao.Properties.IsAlarm.eq(true);
        } else if (newsType == 2) {
            whereCondition = NewsBeanDao.Properties.IsEnterprise.eq(true);
        } else {
            whereCondition = NewsBeanDao.Properties.IsMe.eq(true);
        }
        NewsBeanDao newsBeanDao = CustomerApp.getInstance().getDaoSession().getNewsBeanDao();
        return newsBeanDao.queryBuilder()
                .where(NewsBeanDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
                        , whereCondition)
                .orderDesc(NewsBeanDao.Properties.MessageTime)
                .rx().list()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsBean>>() {
                    @Override
                    public void call(List<NewsBean> newsBeen) {
                        callBack.onFinish();
                        if (newsBeen == null || newsBeen.size() == 0) {
                            callBack.onError("没有消息");
                        } else {
                            callBack.onSuccess(newsBeen);
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Subscription deleteNews(NewsBean newsBean) {
        return CustomerApp.getInstance().getDaoSession().getNewsBeanDao().rx().deleteInTx(newsBean).subscribe();
    }

    @NonNull
    @Override
    public Subscription uploadNewsBeen(List<NewsBean> newsBeen) {
        return CustomerApp.getInstance().getDaoSession().getNewsBeanDao().rx().insertOrReplaceInTx(newsBeen).subscribe();
    }

    @NonNull
    @Override
    public Subscription uploadNewsBean(NewsBean newsBean) {
        return CustomerApp.getInstance().getDaoSession().getNewsBeanDao().rx().insertOrReplaceInTx(newsBean).subscribe();
    }

    @Override
    public long loadUnReadMessageCount() {
//        if (CustomerApp.getInstance().getCurrentUser() == null) {
//            return 0;
//        }
//        return CustomerApp.getInstance().getDaoSession().getNewsBeanDao().queryBuilder()
//                .where(NewsBeanDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
//                        , NewsBeanDao.Properties.IsMe.eq(true)
//                        , NewsBeanDao.Properties.HasRead.eq(false))
//                .count();
        return 0;
    }

    @NonNull
    @Override
    public Subscription getUnreadCount(@NonNull final UnReadCountCallBack callBack) {
        int[] count = new int[4];
        return Observable.just(count)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<int[], Observable<int[]>>() {
                    @Override
                    public Observable<int[]> call(int[] ints) {
                        ints[0] = sp.getInt(CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE, 0);
                        ints[1] = sp.getInt(CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE, 0);
                        ints[2] = sp.getInt(CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE, 0);
                        ints[3] = sp.getInt(CustomerApp.getInstance().getCurrentUser().getUserId() + ConstantStr.NEWS_ME_STATE, 0);
                        return Observable.just(ints);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<int[]>() {
                    @Override
                    public void call(int[] booleans) {
                        callBack.onReadCount(booleans);
                    }
                });
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
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription sendSystemMessage(JSONObject jsonObject, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Count.class)
                .sendMessage(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onSuccess("");
            }

            @Override
            public void onFail() {
                callBack.onFinish();
            }
        }.execute1();
    }

    @Override
    public Subscription getDeptTypeId(@NonNull String types, @NonNull final IListCallBack<DeptType> callBack) {
        Observable<Bean<List<DeptType>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getDeptType(types);
        return new ApiCallBack<List<DeptType>>(observable) {
            @Override
            public void onSuccess(@Nullable List<DeptType> deptTypes) {
                callBack.onFinish();
                callBack.onSuccess(deptTypes);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getFaultReport(long deptId, @NonNull String time, @NonNull final IListCallBack<FaultReport> callBack) {
        Observable<Bean<List<FaultReport>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getFaultReport(deptId, time);
        return new ApiCallBack<List<FaultReport>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultReport> faultReports) {
                callBack.onFinish();
                callBack.onSuccess(faultReports);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getFaultLevel(@NonNull String time, @NonNull final IListCallBack<FaultLevel> callBack) {
        Observable<Bean<List<FaultLevel>>> observable = Api.createRetrofit().create(Api.Count.class)
                .getFaultLevel(time);
        return new ApiCallBack<List<FaultLevel>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultLevel> faultLevels) {
                callBack.onFinish();
                callBack.onSuccess(faultLevels);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getEquipByTaskId(long taskId, @NonNull final IListCallBack<EquipBean> callBack) {
        Observable<Bean<List<EquipBean>>> observable = Api.createRetrofit().create(Api.Check.class)
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
        }.execute1();
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
                        CustomerApp.getInstance().getCurrentUser().setPortraitUrl(userPhotoUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Observable<Bean<String>> observable1 = Api.createRetrofit().create(Api.UserUpdate.class).updateUser(jsonObject.toString());
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
                    }.execute1();
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
        }.execute1();
    }


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
        }.execute1();
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
        }.execute1();
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
        }.execute1();
    }

    @Override
    public Subscription postCidInfo(@NonNull String userCid, @NonNull final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userCid", userCid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Getui.class)
                .postCidInfo(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String s) {
                callBack.onFinish();
                callBack.onError(s);
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
    public Subscription uploadFile(List<String> filePaths, final IObjectCallBack<String> callBack) {
        Observable<Bean<List<String>>> observable = null;
        if (filePaths != null && filePaths.size() > 0) {
            File file = new File(filePaths.get(0));
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("businessType", "sysMessage")
                    .addFormDataPart("fileType", "appendices");
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), requestFile);
            List<MultipartBody.Part> parts = builder.build().parts();
            observable = Api.createRetrofit().create(Api.File.class)
                    .postImageFile(parts);
        }
        if (observable == null) {
            return Observable.just("").subscribe();
        }
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                if (strings == null || strings.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(strings.get(0));
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
    public Subscription getValueAdded(final IObjectCallBack<ValueAddedBean> callBack) {
        return new ApiCallBack<ValueAddedBean>(Api.createRetrofit().create(Api.Count.class).getValueAdded()) {
            @Override
            public void onSuccess(@Nullable ValueAddedBean valueAddedBeans) {
                callBack.onFinish();
                if (valueAddedBeans != null && valueAddedBeans.getList() != null && valueAddedBeans.getList().size() > 0) {
                    callBack.onSuccess(valueAddedBeans);
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
    public Subscription getPartData(String startTime, String endTime, final IObjectCallBack<PartPersonStatistics> callBack) {
        return new ApiCallBack<PartPersonStatistics>(Api.createRetrofit().create(Api.Count.class)
                .getStatisticsUserAndPart(startTime, endTime, 0)) {
            @Override
            public void onSuccess(@Nullable PartPersonStatistics statistics) {
                callBack.onFinish();
                if (statistics != null) {
                    callBack.onSuccess(statistics);
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
    public Subscription getPerson(String startTime, String endTime, final IObjectCallBack<PartPersonStatistics> callBack) {
        return new ApiCallBack<PartPersonStatistics>(Api.createRetrofit().create(Api.Count.class)
                .getStatisticsUserAndPart(startTime, endTime, 1)) {
            @Override
            public void onSuccess(@Nullable PartPersonStatistics statistics) {
                callBack.onFinish();
                if (statistics != null) {
                    callBack.onSuccess(statistics);
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
    public Subscription borrowedSure(long id, int state, final IObjectCallBack<String> callBack) {
        return new ApiCallBack<String>(Api.createRetrofit().create(ToolsApi.class).toolsUserConfirm(id, state)) {
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
    public Subscription getMessageList(Map<String, String> map, final IListCallBack<MessageListBean> callBack) {
        return new ApiCallBack<List<MessageListBean>>(Api.createRetrofit().create(Api.MessageApi.class).getMessageList(map)) {

            @Override
            public void onSuccess(@Nullable List<MessageListBean> strings) {
                callBack.onFinish();
                if (strings != null && strings.size() > 0) {
                    callBack.onSuccess(strings);
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
    public Subscription getMessageDetail(long messageId, final IObjectCallBack<EnterpriseDetailBean> callBack) {
        return new ApiCallBack<EnterpriseDetailBean>(Api.createRetrofit().create(Api.MessageApi.class).getMessageDetail(messageId)) {
            @Override
            public void onSuccess(@Nullable EnterpriseDetailBean s) {
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

    @Override
    public void downLoadFile(final String fileName, final String filePath, String downLoadUrl, @NonNull final DownLoadCallBack callBack) {
        final DownLoadHandle downLoadHandle = new DownLoadHandle(callBack);
        Call<ResponseBody> call = Api.createRetrofit().create(Api.Equip.class).downloadFile(downLoadUrl);
        if (!new File(filePath).exists()) {
            new File(filePath).mkdir();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean isSuccess = writeResponseBodyToDisk(filePath, fileName, response.body());
                    if (isSuccess) {
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString(ConstantStr.KEY_BUNDLE_STR, fileName);
                        bundle.putString(ConstantStr.KEY_BUNDLE_STR_1, filePath);
                        message.setData(bundle);
                        downLoadHandle.sendMessage(message);
                    } else {
                        downLoadHandle.sendEmptyMessage(1);
                    }
                } else {
                    downLoadHandle.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                downLoadHandle.sendEmptyMessage(1);
            }
        });
    }

    @NonNull
    @Override
    public Subscription getFireSaveMessage(long messageId, final IObjectCallBack<MessageListBean> callBack) {
        return new ApiCallBack<MessageListBean>(Api.createRetrofit().create(Api.MessageApi.class).getFireSaveMessage(messageId)) {
            @Override
            public void onSuccess(@Nullable MessageListBean s) {
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

    private boolean writeResponseBodyToDisk(String filePath, String fileName, ResponseBody body) {
        try {
            File futureStudioIconFile = new File(filePath + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static class DownLoadHandle extends Handler {

        private DownLoadCallBack callBack;

        DownLoadHandle(DownLoadCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String fileName = msg.getData().getString(ConstantStr.KEY_BUNDLE_STR);
                String filePath = msg.getData().getString(ConstantStr.KEY_BUNDLE_STR_1);
                callBack.onSuccess(fileName, filePath);
            } else {
                callBack.onFile();
            }
        }
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
    public Subscription register(JSONObject json, final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Login.class).userRegister(json.toString());
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
    public Subscription getRegisterCode(String phoneNum, final IObjectCallBack<String> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Login.class).getRegisterCode(jsonObject.toString());
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
