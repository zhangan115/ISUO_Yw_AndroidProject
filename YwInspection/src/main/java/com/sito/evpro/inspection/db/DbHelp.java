package com.sito.evpro.inspection.db;

import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

/**
 * 数据库帮助类
 * Created by zhangan on 2017-06-09.
 */

public class DbHelp {

    public interface CleanAllDataCallBack {
        /**
         * 清除成功
         */
        void cleanSuccess();

        /**
         * 清除完成
         */
        void cleanFinish();

        /**
         * 清除失败
         */
        void cleanFail();
    }

    @MainThread
    public static void cleanAllData(SharedPreferences sp, @Nullable final CleanAllDataCallBack callBack) {
        sp.edit().clear().apply();//清理所有sp文件数据
    }

}
