package com.sito.evpro.inspection.common;

/**
 * str 常量
 */
public interface ConstantStr {

    String cancelVersion = "cancelVersion";//取消的版本升级
    String USER_CONFIG_FILE = "user_config";
    String APP_HOST = "app_host";
    String NEED_LOGIN = "need_login";
    String CRASH_APP_KEY = "ec4dc006e8";
    String OPTION = "option";
    /**
     * 是否使用过App
     **/
    String USE_APP = "use_app";

    /**
     * APP版本号number(2018-3-2)
     **/
    int VERSION_NO = 8;


    /**
     * 本地数据库名
     **/
    String DATABASE_NAME = "sito_inspection.db";
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
     * 项目数据
     **/
    String PRO_INFO = "pros_info";
    /**
     * 安全包数据
     */
    String SECURIT_INFO = "securit_info";
    /**
     * Bundle Key
     */
    String KEY_BUNDLE_STR = "key_str";
    String KEY_BUNDLE_STR_1 = "key_str_1";
    String KEY_BUNDLE_STR_2 = "key_str_2";
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

    String USER_NAME = "user_name";
    String USER_PASS = "user_pass";

    String COOKIE_DOMAIN = "cookie_domain";
    String COOKIE_NAME = "cookie_name";
    String COOKIE_VALUE = "cookie_value";

    String VOICEURL = "VOICEURL";//录音链接

    String KEY_URL = "key_url";
    String KEY_ID = "key_id";
    String ROOM_START_TIME = "start_time";
    String ROOM_FINISH_TIME = "finish_time";

    String OVERHAUL_NOTE_KEY = "overhaul_note_";

    String WEIXIN_APP_ID = "wx6685b194520e477d";

    String ALPS = "alps";
    String PHONES = "KT50_B2";
    String ROOM_STATE = "check_room_state";

    String START_TYPE_0 = "0";//无要求
    String START_TYPE_1 = "1";//扫描二维码开启流程
    String START_TYPE_2 = "2";//扫描NFC卡开启流程
    String START_TYPE_3 = "3";//扫描RFID开启流程
}