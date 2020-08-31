package com.sito.evpro.inspection.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechUtility;
import com.sito.evpro.inspection.BuildConfig;
import com.sito.evpro.inspection.R;
import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.db.InspectionOpenHelp;
import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.db.DaoMaster;
import com.sito.evpro.inspection.mode.bean.db.DaoSession;
import com.sito.evpro.inspection.mode.bean.employee.EmployeeBean;
import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.inspection.InspectionDetailBean;
import com.sito.evpro.inspection.mode.bean.option.OptionBean;
import com.sito.evpro.inspection.mode.commitinfo.CommitRepositoryComponent;
import com.sito.evpro.inspection.mode.commitinfo.DaggerCommitRepositoryComponent;
import com.sito.evpro.inspection.mode.create.CreateRepositoryComponent;
import com.sito.evpro.inspection.mode.create.DaggerCreateRepositoryComponent;
import com.sito.evpro.inspection.mode.employee.DaggerEmployeeRepositoryComponent;
import com.sito.evpro.inspection.mode.employee.EmployeeRepositoryComponent;
import com.sito.evpro.inspection.mode.equipment.DaggerEquipmentRepositoryComponent;
import com.sito.evpro.inspection.mode.equipment.EquipmentRepositoryComponent;
import com.sito.evpro.inspection.mode.inspection.DaggerInspectionRepositoryComponent;
import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.mode.inspection.work.DaggerInspectionWorkRepositoryComponent;
import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepositoryComponent;
import com.sito.evpro.inspection.utils.Utils;
import com.sito.evpro.inspection.view.ApplicationModule;
import com.sito.evpro.inspection.view.login.LoginActivity;
import com.sito.library.base.AbsBaseApp;
import com.sito.library.utils.Base64Util;
import com.sito.library.utils.SPHelper;
import com.tencent.bugly.crashreport.CrashReport;
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
 * 巡检版App
 * Created by zhangan on 2017-06-21.
 */

public class InspectionApp extends AbsBaseApp {

    private static InspectionApp _instance;
    private User mUser;//当前用户
    private SharedPreferences sp;
    private InspectionRepositoryComponent mRepositoryComponent;
    private CommitRepositoryComponent commitRepositoryComponent;
    private EmployeeRepositoryComponent employeeRepositoryComponent;
    private InspectionWorkRepositoryComponent inspectionWorkRepositoryComponent;
    private CreateRepositoryComponent createRepositoryComponent;
    private EquipmentRepositoryComponent equipmentRepositoryComponent;
    private List<OptionBean> mOptionBeen;
    private List<EquipBean> mEquipBeen;
    private InspectionDetailBean inspectionDetailBean;
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private String workType;
    private Map<Integer, String> workTypeMap = new HashMap<>();
    private Map<Integer, Long> workTypeIdMap = new HashMap<>();
    private Map<Integer, String> workSourceMap = new HashMap<>();
    private Map<Integer, Long> workSourceIdMap = new HashMap<>();
    private Map<Integer, String> faultEquipNameMap = new HashMap<>();
    private Map<Integer, Long> faultEquipIdMap = new HashMap<>();
    private Map<Integer, String> faultTypeMap = new HashMap<>();
    private Map<Integer, Integer> faultTypeIdMap = new HashMap<>();
    private Map<Integer, List<EmployeeBean>> empMap = new HashMap<>();

    private Map<String, Map<String, String>> mapOption;

    public static IWXAPI iwxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        sp = getSharedPreferences(ConstantStr.USER_INFO, Context.MODE_PRIVATE);
        mRepositoryComponent = DaggerInspectionRepositoryComponent.builder().applicationModule(new ApplicationModule(this)).build();
        commitRepositoryComponent = DaggerCommitRepositoryComponent.builder().applicationModule(new ApplicationModule(this)).build();
        employeeRepositoryComponent = DaggerEmployeeRepositoryComponent.builder().applicationModule(new ApplicationModule(this)).build();
        inspectionWorkRepositoryComponent = DaggerInspectionWorkRepositoryComponent.builder().applicationModule(new ApplicationModule(this)).build();
        createRepositoryComponent = DaggerCreateRepositoryComponent.builder().build();
        equipmentRepositoryComponent = DaggerEquipmentRepositoryComponent.builder().build();
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), ConstantStr.CRASH_APP_KEY, BuildConfig.DEBUG);
        }
        initDatabases();
        initSpeech();
        iwxapi = WXAPIFactory.createWXAPI(this, ConstantStr.WEIXIN_APP_ID, true);
        iwxapi.registerApp(ConstantStr.WEIXIN_APP_ID);
        initUmeng();
    }

    private void initUmeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(this, "5a6582a6f43e48782d0000ff", BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    public Map<String, Map<String, String>> getMapOption() {
        if (mapOption != null) {
            return mapOption;
        }
        Map<String, Map<String, String>> mOption = new HashMap<>();
        if (mOptionBeen != null) {
            for (int i = 0; i < mOptionBeen.size(); i++) {
                Map<String, String> map = new HashMap<>();
                String optionId = mOptionBeen.get(i).getOptionId() + "";
                for (int j = 0; j < mOptionBeen.get(i).getItemList().size(); j++) {
                    String itemCode = mOptionBeen.get(i).getItemList().get(j).getItemCode();
                    String itemName = mOptionBeen.get(i).getItemList().get(j).getItemName();
                    map.put(itemCode, itemName);
                }
                mOption.put(optionId, map);
            }
            InspectionApp.getInstance().setMapOption(mOption);
            return mOption;
        } else {
            return null;
        }
    }

    public void setMapOption(Map<String, Map<String, String>> mapOption) {
        this.mapOption = mapOption;
    }

    public Map<Integer, List<EmployeeBean>> getEmpMap() {
        return empMap;
    }

    public void setEmpMap(Map<Integer, List<EmployeeBean>> empMap) {
        this.empMap = empMap;
    }

    public Map<Integer, String> getWorkTypeMap() {
        return workTypeMap;
    }

    public void setWorkTypeMap(Map<Integer, String> workTypeMap) {
        this.workTypeMap = workTypeMap;
    }

    public Map<Integer, Long> getWorkTypeIdMap() {
        return workTypeIdMap;
    }

    public void setWorkTypeIdMap(Map<Integer, Long> workTypeIdMap) {
        this.workTypeIdMap = workTypeIdMap;
    }

    public Map<Integer, String> getWorkSourceMap() {
        return workSourceMap;
    }

    public void setWorkSourceMap(Map<Integer, String> workSourceMap) {
        this.workSourceMap = workSourceMap;
    }

    public Map<Integer, Long> getWorkSourceIdMap() {
        return workSourceIdMap;
    }

    public void setWorkSourceIdMap(Map<Integer, Long> workSourceIdMap) {
        this.workSourceIdMap = workSourceIdMap;
    }

    public Map<Integer, String> getFaultEquipNameMap() {
        return faultEquipNameMap;
    }

    public void setFaultEquipNameMap(Map<Integer, String> faultEquipNameMap) {
        this.faultEquipNameMap = faultEquipNameMap;
    }

    public Map<Integer, Long> getFaultEquipIdMap() {
        return faultEquipIdMap;
    }

    public void setFaultEquipIdMap(Map<Integer, Long> faultEquipIdMap) {
        this.faultEquipIdMap = faultEquipIdMap;
    }

    public Map<Integer, String> getFaultTypeMap() {
        return faultTypeMap;
    }

    public void setFaultTypeMap(Map<Integer, String> faultTypeMap) {
        this.faultTypeMap = faultTypeMap;
    }

    public Map<Integer, Integer> getFaultTypeIdMap() {
        return faultTypeIdMap;
    }

    public void setFaultTypeIdMap(Map<Integer, Integer> faultTypeIdMap) {
        this.faultTypeIdMap = faultTypeIdMap;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }


    private void initSpeech() {
        SpeechUtility.createUtility(getApplicationContext(), "appid=" + "594a2e41");
    }

    /**
     * 初始化数据库
     */
    private void initDatabases() {
        InspectionOpenHelp mHelper = new InspectionOpenHelp(this, "inspection_db");
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
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
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Utils.showToast(this, message, Toast.LENGTH_SHORT, -1);
    }

    public static InspectionApp getInstance() {
        return _instance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 设置当前用户
     *
     * @param user 用户
     */
    public void setCurrentUser(@NonNull User user) {
        this.mUser = user;
        String userInfo = new Gson().toJson(user);
        SPHelper.write(this, ConstantStr.USER_INFO, ConstantStr.USER_BEAN, userInfo);
    }

    /**
     * 保存字典
     *
     * @param list
     */
    public void setOptionInfo(@NonNull List<OptionBean> list) {
        mOptionBeen = new ArrayList<>();
        mOptionBeen.addAll(list);
        String jsonStr = new Gson().toJson(list);
        SPHelper.write(this, ConstantStr.USER_INFO, ConstantStr.OPTION, jsonStr);
    }

    /**
     * 获取字典
     *
     * @return
     */
    public List<OptionBean> getOptionInfo() {
        if (mOptionBeen == null) {
            String optionStr = SPHelper.readString(this, ConstantStr.USER_INFO, ConstantStr.OPTION);
            Type type = new TypeToken<List<OptionBean>>() {
            }.getType();
            mOptionBeen = new Gson().fromJson(optionStr, type);
        }
        return mOptionBeen;
    }

    /**
     * 保存设备
     *
     * @param list
     */
    public void setEquipInfo(@NonNull List<EquipBean> list) {
        mEquipBeen = new ArrayList<>();
        mEquipBeen.addAll(list);
    }

    public List<EquipBean> getEquipInfo() {
        return mEquipBeen;
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
        employeeRepositoryComponent.getRepository().cleanCache();
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_NAME);
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_BEAN);
        SPHelper.remove(this, ConstantStr.USER_INFO, ConstantStr.USER_INFO);
        mUser = null;
    }

    public InspectionRepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }

    public CommitRepositoryComponent getCommitRepositoryComponent() {
        return commitRepositoryComponent;
    }

    public EmployeeRepositoryComponent getEmployeeRepositoryComponent() {
        return employeeRepositoryComponent;
    }

    public InspectionWorkRepositoryComponent getInspectionWorkRepositoryComponent() {
        return inspectionWorkRepositoryComponent;
    }

    public CreateRepositoryComponent getCreateRepositoryComponent() {
        return createRepositoryComponent;
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

    public String voiceCacheFile() {
        String voiceCacheDir = getExternalCacheDir().getAbsolutePath() + File.separator + "voiceCache" + File.separator;
        if (!new File(voiceCacheDir).exists()) {
            new File(voiceCacheDir).mkdir();
        }
        return voiceCacheDir;
    }

    @Override
    public Intent needLoginIntent() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setAction(ConstantStr.NEED_LOGIN);
        return intent;
    }

    public int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public InspectionDetailBean getInspectionDetailBean() {
        return inspectionDetailBean;
    }

    public void setInspectionDetailBean(InspectionDetailBean uploadInspectionBean) {
        this.inspectionDetailBean = uploadInspectionBean;
    }
}
