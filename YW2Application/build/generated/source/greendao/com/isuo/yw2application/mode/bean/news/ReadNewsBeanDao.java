package com.isuo.yw2application.mode.bean.news;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.isuo.yw2application.mode.bean.db.DaoSession;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "read_news".
*/
public class ReadNewsBeanDao extends AbstractDao<ReadNewsBean, Void> {

    public static final String TABLENAME = "read_news";

    /**
     * Properties of entity ReadNewsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MessageId = new Property(0, long.class, "messageId", false, "MESSAGE_ID");
        public final static Property IsRead = new Property(1, boolean.class, "isRead", false, "IS_READ");
        public final static Property CreateTime = new Property(2, long.class, "createTime", false, "CREATE_TIME");
        public final static Property MessageType = new Property(3, int.class, "messageType", false, "MESSAGE_TYPE");
        public final static Property UserId = new Property(4, int.class, "userId", false, "USER_ID");
    }


    public ReadNewsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ReadNewsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"read_news\" (" + //
                "\"MESSAGE_ID\" INTEGER NOT NULL ," + // 0: messageId
                "\"IS_READ\" INTEGER NOT NULL ," + // 1: isRead
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 2: createTime
                "\"MESSAGE_TYPE\" INTEGER NOT NULL ," + // 3: messageType
                "\"USER_ID\" INTEGER NOT NULL );"); // 4: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"read_news\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ReadNewsBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getMessageId());
        stmt.bindLong(2, entity.getIsRead() ? 1L: 0L);
        stmt.bindLong(3, entity.getCreateTime());
        stmt.bindLong(4, entity.getMessageType());
        stmt.bindLong(5, entity.getUserId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ReadNewsBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getMessageId());
        stmt.bindLong(2, entity.getIsRead() ? 1L: 0L);
        stmt.bindLong(3, entity.getCreateTime());
        stmt.bindLong(4, entity.getMessageType());
        stmt.bindLong(5, entity.getUserId());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ReadNewsBean readEntity(Cursor cursor, int offset) {
        ReadNewsBean entity = new ReadNewsBean( //
            cursor.getLong(offset + 0), // messageId
            cursor.getShort(offset + 1) != 0, // isRead
            cursor.getLong(offset + 2), // createTime
            cursor.getInt(offset + 3), // messageType
            cursor.getInt(offset + 4) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ReadNewsBean entity, int offset) {
        entity.setMessageId(cursor.getLong(offset + 0));
        entity.setIsRead(cursor.getShort(offset + 1) != 0);
        entity.setCreateTime(cursor.getLong(offset + 2));
        entity.setMessageType(cursor.getInt(offset + 3));
        entity.setUserId(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ReadNewsBean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ReadNewsBean entity) {
        return null;
    }

    @Override
    public boolean hasKey(ReadNewsBean entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
