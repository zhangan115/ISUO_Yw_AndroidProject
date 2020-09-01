package com.sito.customer.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechUtility;
import com.igexin.sdk.PushManager;
import com.sito.customer.BuildConfig;
import com.sito.customer.R;
import com.sito.customer.api.Api;
import com.sito.customer.common.BroadcastAction;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.db.CustomerOpenHelp;
import com.sito.customer.mode.bean.User;

import com.sito.customer.mode.bean.news.DaoMaster;
import com.sito.customer.mode.bean.news.DaoSession;
import com.sito.customer.mode.bean.news.MessageContent;
import com.sito.customer.mode.bean.news.NewsBean;
import com.sito.customer.mode.bean.option.OptionBean;
import com.sito.customer.mode.count.CountRepositoryComponent;
import com.sito.customer.mode.count.DaggerCountRepositoryComponent;
import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.mode.customer.DaggerCustomerRepositoryComponent;
import com.sito.customer.mode.equipment.DaggerEquipmentRepositoryComponent;
import com.sito.customer.mode.equipment.EquipmentRepositoryComponent;
import com.sito.customer.mode.fault.DaggerFaultRepositoryComponent;
import com.sito.customer.mode.fault.FaultRepositoryComponent;
import com.sito.customer.mode.generate.DaggerGenerateRepositoryComponent;
import com.sito.customer.mode.generate.GenerateRepositoryComponent;
import com.sito.customer.mode.work.DaggerWorkRepositoryComponent;
import com.sito.customer.mode.work.WorkRepositoryComponent;
import com.sito.customer.push.CustIntentService;
import com.sito.customer.push.CustPushService;
import com.sito.customer.utils.NewsUtils;
import com.sito.customer.utils.Utils;
import com.sito.customer.view.ApplicationModule;
import com.sito.customer.view.home.HomeActivity;
import com.sito.customer.view.login.LoginActivity;
import com.sito.customer.view.splash.SplashActivity;
import com.sito.library.base.AbsBaseApp;
import com.sito.library.utils.Base64Util;
import com.sito.library.utils.SPHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户版App
 * Created by zhangan on 2017-06-21.
 */

public class CustomerApp extends AbsBaseApp {

    private static CustomerApp _instance;
    private User mUser;//当前用户
    private ArrayList<OptionBean> mOptionBeen;
    private SharedPreferences sp;
    private Map<String, Map<String, String>> mapOption;
    private String cid = "";

    private static DaoSession mDaoSession;
    public static IWXAPI iwxapi;
    public static boolean isNewsMeOpen, isNewsOtherOpen;
    private AppStatusTracker appStatusTracker;
    //reposition component
    private CustomerRepositoryComponent mRepositoryComponent;
    private WorkRepositoryComponent workRepositoryComponent;
    private FaultRepositoryComponent faultRepositoryComponent;
    private CountRepositoryComponent countRepositoryComponent;
    private GenerateRepositoryComponent generateRepositoryComponent;
    private EquipmentRepositoryComponent equipmentRepositoryComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        sp = getSharedPreferences(ConstantStr.USER_INFO, Context.MODE_PRIVATE);
        ApplicationModule applicationModule = new ApplicationModule(this);
        mRepositoryComponent = DaggerCustomerRepositoryComponent.builder().applicationModule(applicationModule).build();
        workRepositoryComponent = DaggerWorkRepositoryComponent.builder().applicationModule(applicationModule).build();
        faultRepositoryComponent = DaggerFaultRepositoryComponent.builder().applicationModule(applicationModule).build();
        countRepositoryComponent = DaggerCountRepositoryComponent.builder().applicationModule(applicationModule).build();
        generateRepositoryComponent = DaggerGenerateRepositoryComponent.builder().build();
        equipmentRepositoryComponent = DaggerEquipmentRepositoryComponent.builder().build();
        initDatabases();
        initGetuiPush();
        initSpeech();
        iwxapi = WXAPIFactory.createWXAPI(this, ConstantStr.WEIXIN_APP_ID, true);
        iwxapi.registerApp(ConstantStr.WEIXIN_APP_ID);
        appStatusTracker = new AppStatusTracker(this);
        initUmeng();
    }

    private void initUmeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(this, "5f4a3ce412981d3ca30a79cf", BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    private void initGetuiPush() {
        PushManager.getInstance().initialize(this.getApplicationContext(), CustPushService.class);
        PushManager.getInstance().registerPushIntentService(this, CustIntentService.class);
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    /**
     * 初始化数据库
     */
    private void initDatabases() {
        CustomerOpenHelp mHelper = new CustomerOpenHelp(this, "cusomer_db");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    @Override
    public String AppHost() {
        return SPHelper.readString(this, ConstantStr.USER_INFO, ConstantStr.APP_HOST, Api.HOST);
    }

    public void editHost(String host) {
        SPHelper.write(this, ConstantStr.USER_INFO, ConstantStr.APP_HOST, host);
    }

    @Override
    public void showToast(@NonNull String message) {
        Utils.showToast(this, message);
    }

    public static CustomerApp getInstance() {
        return _instance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 设置当前用户
     *
     * @param user 用户
     */
    public void setCurrentUser(User user) {
        this.mUser = user;
        if (user != null) {
            String userInfo = new Gson().toJson(user);
            sp.edit().putString(ConstantStr.USER_BEAN, userInfo).apply();
        }
    }

    public Map<String, Map<String, String>> getMapOption() {
        if (mapOption != null) {
            return mapOption;
        }
        Map<String, Map<String, String>> mOption = new HashMap<>();
        for (int i = 0; i < getOptionInfo().size(); i++) {
            Map<String, String> map = new HashMap<>();
            String optionId = getOptionInfo().get(i).getOptionId() + "";
            for (int j = 0; j < getOptionInfo().get(i).getItemList().size(); j++) {
                String itemCode = getOptionInfo().get(i).getItemList().get(j).getItemCode();
                String itemName = getOptionInfo().get(i).getItemList().get(j).getItemName();
                map.put(itemCode, itemName);
            }
            mOption.put(optionId, map);
        }
        CustomerApp.getInstance().setMapOption(mOption);
        return mOption;
    }

    public void setMapOption(Map<String, Map<String, String>> mapOption) {
        this.mapOption = mapOption;
    }

    /**
     * 获取当前用户
     *
     * @return 用户
     */
    public User getCurrentUser() {
        if (mUser == null) {
            String userInfo = SPHelper.readString(getApplicationContext(), ConstantStr.USER_INFO, ConstantStr.USER_BEAN);
            if (!TextUtils.isEmpty(userInfo)) {
                mUser = new Gson().fromJson(userInfo, User.class);
            }
        }
        return mUser;
    }

    /**
     * 退出当前用户
     */
    public void exitCurrentUser() {
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_NAME);
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_BEAN);
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_INFO);
        mUser = null;
    }

    public CustomerRepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }

    public FaultRepositoryComponent getFaultRepositoryComponent() {
        return faultRepositoryComponent;
    }

    public CountRepositoryComponent getCountRepositoryComponent() {
        return countRepositoryComponent;
    }

    public GenerateRepositoryComponent getGenerateRepositoryComponent() {
        return generateRepositoryComponent;
    }

    public EquipmentRepositoryComponent getEquipmentRepositoryComponent() {
        return equipmentRepositoryComponent;
    }

    /**
     * @return 获取用户名称
     */
    @Nullable
    public String getUserName() {
        String name = null;
        try {
            name = new String(Base64Util.decode(sp.getString(ConstantStr.USER_NAME, "")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @return 获取用户密码
     */
    @Nullable
    public String getUserPass() {
        String pass = null;
        try {
            pass = new String(Base64Util.decode(sp.getString(ConstantStr.USER_PASS, "")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return pass;
    }

    @NonNull
    @Override
    public String imageCacheFile() {
        return getExternalCacheDir().getAbsolutePath();
    }

    @Override
    public Intent needLoginIntent() {
        exitCurrentUser();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setAction(ConstantStr.NEED_LOGIN);
        return intent;
    }

    public WorkRepositoryComponent getWorkRepositoryComponent() {
        return workRepositoryComponent;
    }

    public int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 保存字典
     *
     * @param list 字典数据
     */
    public void setOptionInfo(@NonNull List<OptionBean> list) {
        mOptionBeen = new ArrayList<>();
        String optionStr = new Gson().toJson(list);
        mOptionBeen.addAll(list);
        sp.edit().putString(ConstantStr.OPTION_INFO, optionStr).apply();
    }

    private void initSpeech() {
        SpeechUtility.createUtility(getApplicationContext(), "appid=" + "594a2e41");
    }

    public String voiceCacheFile() {
        String voiceCacheDir = getExternalCacheDir().getAbsolutePath() + File.separator + "voiceCache" + File.separator;
        if (!new File(voiceCacheDir).exists()) {
            new File(voiceCacheDir).mkdir();
        }
        return voiceCacheDir;
    }

    public ArrayList<OptionBean> getOptionInfo() {
        if (mOptionBeen == null) {
            Type typeToken = new TypeToken<List<OptionBean>>() {
            }.getType();
            mOptionBeen = new Gson().fromJson(sp.getString(ConstantStr.OPTION_INFO, null), typeToken);
        }
        return mOptionBeen;
    }

    private long msgTime, noNotifyTime;
    private int msgCount;
    private int sendCount;
    private int messageCount = 1;

    public synchronized void sendMessage(String msg) {
        messageCount++;
        if (System.currentTimeMillis() - msgTime < 5000) {//推送间隔小于5s，只连续响声三次
            sendCount++;
        }
        if (msgCount < 3) {
            msgTime = System.currentTimeMillis();
            msgCount++;
        } else {
            if (noNotifyTime == 0) {
                noNotifyTime = System.currentTimeMillis();
            }
        }
        if (System.currentTimeMillis() - noNotifyTime > 20 * 1000 && noNotifyTime != 0) {
            msgCount = 0;
            noNotifyTime = 0;
            sendCount = 0;
        }
        MessageContent messageContent = new Gson().fromJson(msg, MessageContent.class);
        messageContent.setAppContent(msg);
        if (messageContent.getMessageType() == 2) {
            NewsBean newsBean = NewsUtils.getNewsBean(messageContent);
            if (newsBean != null) {
                Intent notifyIntent;
                if (AppStatusManager.getInstance().getAppStatus() == AppStatusConstant.STATUS_FORCE_KILLED) {
                    // no application live
                    notifyIntent = new Intent(this, SplashActivity.class);
                    sendNotification(newsBean.getNotifyContent(), NewsUtils.getNewsNotifyDraw(newsBean, -1), notifyIntent, newsBean);
                } else if (appStatusTracker.isForGround()) {
                    // app is live
                    if (!isNewsMeOpen) {
                        if (newsBean.isMe()) {
                            startHomeActivity(newsBean);
                        } else {
                            if (!isNewsOtherOpen) {
                                startHomeActivity(newsBean);
                            }
                        }
                    }
                } else {
                    startHomeActivity(newsBean);
                }
                sendNewsBeanToView(newsBean);
            }
        }
    }

    private void startHomeActivity(NewsBean newsBean) {
        Intent notifyIntent = new Intent(this, HomeActivity.class);
        notifyIntent.setAction(HomeActivity.HOME_ACTION_OPEN_NEWS);
        notifyIntent.putExtra(ConstantStr.KEY_BUNDLE_INT, NewsUtils.getNewsIntent(newsBean));
        sendNotification(newsBean.getNotifyContent(), NewsUtils.getNewsNotifyDraw(newsBean, -1), notifyIntent, newsBean);
    }

    /**
     * 发送新消息
     *
     * @param newsBean 新消息
     */
    private void sendNewsBeanToView(NewsBean newsBean) {
        if (getCurrentUser() != null) {
            if (newsBean.isAlarm()) {
                int count = SPHelper.readInt(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE);
                ++count;
                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE, count);
            }
            if (newsBean.isWork()) {
                int count = SPHelper.readInt(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE);
                ++count;
                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE, count);
            }
            if (newsBean.isEnterprise()) {
                int count = SPHelper.readInt(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE);
                ++count;
                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE, count);
            }
            if (newsBean.isMe()) {
                int count = SPHelper.readInt(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_ME_STATE);
                ++count;
                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_ME_STATE, count);
            }
        }

        Intent intent = new Intent();
        intent.setAction(BroadcastAction.MESSAGE_UN_READ_STATE);
        sendBroadcast(intent);

        Intent newMessageInt = new Intent();
        newMessageInt.setAction(BroadcastAction.MESSAGE_NEW_MESSAGE);
        newMessageInt.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, newsBean);
        sendBroadcast(newMessageInt);
    }

    public void sendNotification(String message, @DrawableRes int drawRes, @Nullable Intent intent, NewsBean newsBean) {
        int notifyId;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        if (newsBean.getNotifyId() == 1) {
            notifyId = 1;
        } else {
            notifyId = messageCount;
        }
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(ConstantStr.NOTIFICATION_CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(ConstantStr.NOTIFICATION_CHANNEL_ID
                        , ConstantStr.NOTIFICATION_CHANNEL_NAME
                        , NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(ConstantStr.NOTIFICATION_CHANNEL_DESCRIPTION);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setShowBadge(true);
                channel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(this, ConstantStr.NOTIFICATION_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        PendingIntent pendingIntent;
        if (intent == null) {
            intent = new Intent(this, SplashActivity.class);
        }
        if (sendCount < 3) {
            builder.setDefaults(Notification.DEFAULT_ALL);//设置默认的声音和震动
        }
        pendingIntent = PendingIntent.getActivity(this, messageCount, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.evpro_notif)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), drawRes))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getResources().getColor(R.color.colorPrimary));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            builder.setGroupSummary(true);
            builder.setGroup(ConstantStr.NOTIFY_GROUP);
        }
        Notification notification = builder.build();
        notificationManager.notify(notifyId, notification);
    }

    public void cleanNotify(int notifyId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(notifyId);
        }
    }
}
