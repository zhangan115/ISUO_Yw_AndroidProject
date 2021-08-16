package com.isuo.yw2application.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.isuo.yw2application.mode.bean.db.CreateEquipmentDbDao;
import com.isuo.yw2application.mode.bean.db.CreateRoomDbDao;
import com.isuo.yw2application.mode.bean.db.DaoMaster;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDbDao;
import com.isuo.yw2application.mode.bean.db.EquipmentDbDao;
import com.isuo.yw2application.mode.bean.db.ImageDao;
import com.isuo.yw2application.mode.bean.db.InspectionTaskDbDao;
import com.isuo.yw2application.mode.bean.db.NewsBeanDao;
import com.isuo.yw2application.mode.bean.db.RoomDbDao;
import com.isuo.yw2application.mode.bean.db.ShareDataDbDao;
import com.isuo.yw2application.mode.bean.db.TaskDbDao;
import com.isuo.yw2application.mode.bean.db.UserInfoDao;
import com.isuo.yw2application.mode.bean.db.VoiceDao;



import org.greenrobot.greendao.database.Database;


/**
 * 数据库升级类
 */


public class CustomerOpenHelp extends DaoMaster.OpenHelper {

    public CustomerOpenHelp(Context context, String name) {
        super(context, name);
    }

    public CustomerOpenHelp(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, NewsBeanDao.class, VoiceDao.class, ImageDao.class, EquipmentDbDao.class
                , EquipmentDataDbDao.class, RoomDbDao.class, ShareDataDbDao.class
                , UserInfoDao.class, TaskDbDao.class
                , CreateEquipmentDbDao.class, CreateRoomDbDao.class, InspectionTaskDbDao.class);
    }

}
