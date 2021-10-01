package com.isuo.yw2application.common;

/**
 * str 常量
 */
public interface ConstantStr {

    String cancelVersion = "cancelVersion";//取消的版本升级
    String USER_CONFIG_FILE = "user_config";
    String APP_HOST = "app_host";
    String NEED_LOGIN = "need_login";
    String CRASH_APP_KEY = "d614de822d";
    String SPEED_APPID = "appid=594a2e41";
    String UMConfigure_Key = "5f4a3ce412981d3ca30a79cf";
    String NOTIFICATION_CHANNEL_NAME = "ISUO";
    String NOTIFICATION_CHANNEL_DESCRIPTION = "消息提示";
    String NOTIFICATION_CHANNEL_ID = "isuo_news_id";

    /**
     * 是否使用过App
     **/
    String USE_APP = "use_app";

    /**
     * APP版本号number
     **/
    int VERSION_NO = 6;


    String NOTIFY_GROUP = "notify_group";


    /**
     * 本地数据库名
     **/
    String DATABASE_NAME = "isuo_yw.db";
    /**
     * 本地数据库版本号
     **/
    int DATABASE_VERSION = 1;
    /**
     * 登录账户的信息key值
     **/
    String PROFILE_ACCOUNT = "account";
    /**
     * 消息临时存储
     **/
    String NEWS_COUNT = "news_count";
    /**
     * 设置ip信息
     **/
    String IP_INFO = "ip_info";
    /**
     * user store
     **/
    String USER_STORE = "user_store";
    /**
     * user store
     **/
    String USER_DATA = "user_data";
    /**
     * user信息key值
     **/
    String USER_INFO = "user_info";
    /**
     * user信息key值
     **/
    String USER_BEAN = "user_bean";
    /**
     * 字典数据
     **/
    String OPTION_INFO = "option_info";

    String SECURITY_INFO = "security_info";
    String SECURITY_OVERHAUL_INFO = "security_overhaul_info";

    /**
     * Bundle Key
     */
    String KEY_BUNDLE_STR = "key_str";
    String KEY_BUNDLE_STR_1 = "key_str_1";
    String KEY_BUNDLE_STR_2 = "key_str_2";
    String KEY_BUNDLE_STR_3 = "key_str_3";
    String KEY_BUNDLE_INT = "key_int";
    String KEY_BUNDLE_INT_1 = "key_int_1";
    String KEY_BUNDLE_INT_2 = "key_int_2";
    String KEY_BUNDLE_LONG = "key_long";
    String KEY_BUNDLE_LONG_1 = "key_long_1";
    String KEY_BUNDLE_LONG_2 = "key_long_2";
    String KEY_BUNDLE_OBJECT = "key_object";
    String KEY_BUNDLE_OBJECT_1 = "key_object_1";
    String KEY_BUNDLE_OBJECT_2 = "key_object_2";
    String KEY_BUNDLE_LIST = "key_list";
    String KEY_BUNDLE_LIST_1 = "key_list_1";
    String KEY_BUNDLE_LIST_2 = "key_list_2";
    String KEY_BUNDLE_TITLE = "key_title";
    String KEY_BUNDLE_BOOLEAN = "key_boolean";
    String KEY_BUNDLE_BOOLEAN_1 = "key_boolean_1";
    String KEY_BUNDLE_BOOLEAN_2 = "key_boolean_2";
    String KEY_BUNDLE_BOOLEAN_3 = "key_boolean_3";

    String USER_NAME = "user_name";
    String USER_PASS = "user_pass";

    String COOKIE_DOMAIN = "cookie_domain";
    String COOKIE_NAME = "cookie_name";
    String COOKIE_VALUE = "cookie_value";

    String VOICEURL = "VOICEURL";//录音链接

    String KEY_URL = "key_url";
    String KEY_ID = "key_id";
    String KEY_TITLE = "key_title";
    String WEIXIN_APP_ID = "wxba4dd4de1d07f517";

    String KEY_NEW_BEAN = "news_bean";
    String KEY_NEW_ID = "news_notify_id";
    String OPEN_NEWS_UI = "open_news_ui";

    String NEWS_ALARM_STATE = "news_alarm_state_1.5";
    String NEWS_WORK_STATE = "news_work_state_1.5";
    String NEWS_NOTIFY_STATE = "news_notify_state_1.5";
    String NEWS_ME_STATE = "news_me_state_1.5";

    String START_TYPE_0 = "0";//无要求
    String START_TYPE_1 = "1";//扫描二维码开启流程
    String START_TYPE_2 = "2";//扫描NFC卡开启流程
    String START_TYPE_3 = "3";//扫描RFID开启流程

    String ROOM_START_TIME = "start_time";
    String ROOM_FINISH_TIME = "finish_time";

    String ALPS = "alps";

    String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";    //解码广播
    String START_SCAN_ACTION = "com.geomobile.se4500barcode";    //调用扫描广播
    String STOP_SCAN = "com.geomobile.se4500barcodestop";    //停止扫描广播

    String WORK_ITEM = "work_item";

    String INSPECTION_CACHE_DATA = "inspection_cache_data";
    String INSPECTION_KEY_EQUIP = "inspection_cache_equip";
    String INSPECTION_KEY_DATA = "inspection_cache_all_data";
}