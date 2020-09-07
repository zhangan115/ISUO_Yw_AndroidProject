package com.isuo.yw2application.mode.bean.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "create_room".
*/
public class CreateRoomDbDao extends AbstractDao<CreateRoomDb, Long> {

    public static final String TABLENAME = "create_room";

    /**
     * Properties of entity CreateRoomDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property RoomId = new Property(1, Long.class, "roomId", false, "ROOM_ID");
        public final static Property RoomName = new Property(2, String.class, "roomName", false, "ROOM_NAME");
        public final static Property RoomType = new Property(3, int.class, "roomType", false, "ROOM_TYPE");
        public final static Property CreateTime = new Property(4, long.class, "createTime", false, "CREATE_TIME");
        public final static Property NeedUpload = new Property(5, boolean.class, "needUpload", false, "NEED_UPLOAD");
        public final static Property CurrentUserId = new Property(6, long.class, "currentUserId", false, "CURRENT_USER_ID");
    }


    public CreateRoomDbDao(DaoConfig config) {
        super(config);
    }
    
    public CreateRoomDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"create_room\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"ROOM_ID\" INTEGER," + // 1: roomId
                "\"ROOM_NAME\" TEXT," + // 2: roomName
                "\"ROOM_TYPE\" INTEGER NOT NULL ," + // 3: roomType
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 4: createTime
                "\"NEED_UPLOAD\" INTEGER NOT NULL ," + // 5: needUpload
                "\"CURRENT_USER_ID\" INTEGER NOT NULL );"); // 6: currentUserId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"create_room\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CreateRoomDb entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(2, roomId);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(3, roomName);
        }
        stmt.bindLong(4, entity.getRoomType());
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getNeedUpload() ? 1L: 0L);
        stmt.bindLong(7, entity.getCurrentUserId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CreateRoomDb entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(2, roomId);
        }
 
        String roomName = entity.getRoomName();
        if (roomName != null) {
            stmt.bindString(3, roomName);
        }
        stmt.bindLong(4, entity.getRoomType());
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getNeedUpload() ? 1L: 0L);
        stmt.bindLong(7, entity.getCurrentUserId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CreateRoomDb readEntity(Cursor cursor, int offset) {
        CreateRoomDb entity = new CreateRoomDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // roomId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // roomName
            cursor.getInt(offset + 3), // roomType
            cursor.getLong(offset + 4), // createTime
            cursor.getShort(offset + 5) != 0, // needUpload
            cursor.getLong(offset + 6) // currentUserId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CreateRoomDb entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRoomId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRoomName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRoomType(cursor.getInt(offset + 3));
        entity.setCreateTime(cursor.getLong(offset + 4));
        entity.setNeedUpload(cursor.getShort(offset + 5) != 0);
        entity.setCurrentUserId(cursor.getLong(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CreateRoomDb entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CreateRoomDb entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CreateRoomDb entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
