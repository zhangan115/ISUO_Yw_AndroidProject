package com.isuo.yw2application.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechUtility;
import com.igexin.sdk.PushManager;
import com.isuo.yw2application.BuildConfig;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.db.CustomerOpenHelp;
import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.news.DaoMaster;
import com.isuo.yw2application.mode.bean.news.DaoSession;
import com.isuo.yw2application.mode.bean.option.OptionBean;
import com.isuo.yw2application.mode.count.CountRepositoryComponent;
import com.isuo.yw2application.mode.count.DaggerCountRepositoryComponent;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.mode.customer.DaggerCustomerRepositoryComponent;
import com.isuo.yw2application.mode.equipment.DaggerEquipmentRepositoryComponent;
import com.isuo.yw2application.mode.equipment.EquipmentRepositoryComponent;
import com.isuo.yw2application.mode.fault.DaggerFaultRepositoryComponent;
import com.isuo.yw2application.mode.fault.FaultRepositoryComponent;
import com.isuo.yw2application.mode.generate.DaggerGenerateRepositoryComponent;
import com.isuo.yw2application.mode.generate.GenerateRepositoryComponent;
import com.isuo.yw2application.mode.work.DaggerWorkRepositoryComponent;
import com.isuo.yw2application.mode.work.WorkRepositoryComponent;
import com.isuo.yw2application.push.CustIntentService;
import com.isuo.yw2application.push.CustPushService;
import com.isuo.yw2application.utils.Utils;
import com.isuo.yw2application.view.ApplicationModule;
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

public class Yw2Application extends AbsBaseApp {

    private static Yw2Application _instance;
    private User mUser;//当前用户
    private ArrayList<OptionBean> mOptionBeen;
    private SharedPreferences sp;
    private Map<String, Map<String, String>> mapOption;
    private String cid = "";

    private static DaoSession mDaoSession;
    public static IWXAPI iwxapi;
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
        initUmeng();
    }

    public synchronized void sendMessage(String msg) {

    }

    /**
     * 初始化数据库
     */
    private void initDatabases() {
        CustomerOpenHelp mHelper = new CustomerOpenHelp(this, "yw2_db");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    private void initSpeech() {
        SpeechUtility.createUtility(getApplicationContext(), "appid=" + "594a2e41");
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

    @Override
    public String AppHost() {
        return null;
    }

    @Override
    public void showToast(@NonNull String message) {
        Utils.showToast(this, message);
    }

    @NonNull
    @Override
    public String imageCacheFile() {
        return getExternalCacheDir().getAbsolutePath();
    }

    @Override
    public Intent needLoginIntent() {
        return null;
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

    public DaoSession getDaoSession() {
        return mDaoSession;
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

    public String voiceCacheFile() {
        String voiceCacheDir = getExternalCacheDir().getAbsolutePath() + File.separator + "voiceCache" + File.separator;
        if (!new File(voiceCacheDir).exists()) {
            new File(voiceCacheDir).mkdir();
        }
        return voiceCacheDir;
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
        this.setMapOption(mOption);
        return mOption;
    }

    public void setMapOption(Map<String, Map<String, String>> mapOption) {
        this.mapOption = mapOption;
    }

    public ArrayList<OptionBean> getOptionInfo() {
        if (mOptionBeen == null) {
            Type typeToken = new TypeToken<List<OptionBean>>() {
            }.getType();
            mOptionBeen = new Gson().fromJson(sp.getString(ConstantStr.OPTION_INFO, null), typeToken);
        }
        return mOptionBeen;
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

    public WorkRepositoryComponent getWorkRepositoryComponent() {
        return workRepositoryComponent;
    }

    public int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public static Yw2Application getInstance() {
        return _instance;
    }
}
