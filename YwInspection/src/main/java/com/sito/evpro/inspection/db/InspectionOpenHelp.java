package com.sito.evpro.inspection.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.sito.evpro.inspection.mode.bean.db.CreateEquipmentDbDao;
import com.sito.evpro.inspection.mode.bean.db.CreateRoomDbDao;
import com.sito.evpro.inspection.mode.bean.db.DaoMaster;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDataDbDao;
import com.sito.evpro.inspection.mode.bean.db.EquipmentDbDao;
import com.sito.evpro.inspection.mode.bean.db.ImageDao;
import com.sito.evpro.inspection.mode.bean.db.RoomDbDao;
import com.sito.evpro.inspection.mode.bean.db.TaskDbDao;
import com.sito.evpro.inspection.mode.bean.db.UserInfoDao;
import com.sito.evpro.inspection.mode.bean.db.VoiceDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库升级类
 */


public class InspectionOpenHelp extends DaoMaster.OpenHelper {

    public InspectionOpenHelp(Context context, String name) {
        super(context, name);
    }

    public InspectionOpenHelp(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, VoiceDao.class, ImageDao.class, EquipmentDbDao.class, EquipmentDataDbDao.class, RoomDbDao.class
                , UserInfoDao.class, TaskDbDao.class, CreateEquipmentDbDao.class, CreateRoomDbDao.class);
    }
}
