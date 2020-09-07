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
 * DAO for table "file_news".
*/
public class NewsBeanDao extends AbstractDao<NewsBean, Long> {

    public static final String TABLENAME = "file_news";

    /**
     * Properties of entity NewsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Tip = new Property(1, String.class, "tip", false, "TIP");
        public final static Property ContentInfo = new Property(2, String.class, "contentInfo", false, "CONTENT_INFO");
        public final static Property FromUser = new Property(3, String.class, "fromUser", false, "FROM_USER");
        public final static Property NewsContent = new Property(4, String.class, "newsContent", false, "NEWS_CONTENT");
        public final static Property MeContent = new Property(5, String.class, "meContent", false, "ME_CONTENT");
        public final static Property Title = new Property(6, String.class, "title", false, "TITLE");
        public final static Property TaskId = new Property(7, Long.class, "taskId", false, "TASK_ID");
        public final static Property MessageTime = new Property(8, long.class, "messageTime", false, "MESSAGE_TIME");
        public final static Property MessageType = new Property(9, int.class, "messageType", false, "MESSAGE_TYPE");
        public final static Property ToUserId = new Property(10, int.class, "toUserId", false, "TO_USER_ID");
        public final static Property NewsType = new Property(11, int.class, "newsType", false, "NEWS_TYPE");
        public final static Property SmallType = new Property(12, int.class, "smallType", false, "SMALL_TYPE");
        public final static Property HasRead = new Property(13, boolean.class, "hasRead", false, "HAS_READ");
        public final static Property IsMe = new Property(14, boolean.class, "isMe", false, "IS_ME");
        public final static Property IsAlarm = new Property(15, boolean.class, "isAlarm", false, "IS_ALARM");
        public final static Property IsWork = new Property(16, boolean.class, "isWork", false, "IS_WORK");
        public final static Property IsEnterprise = new Property(17, boolean.class, "isEnterprise", false, "IS_ENTERPRISE");
        public final static Property CurrentUserId = new Property(18, int.class, "currentUserId", false, "CURRENT_USER_ID");
    }


    public NewsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public NewsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"file_news\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"TIP\" TEXT," + // 1: tip
                "\"CONTENT_INFO\" TEXT," + // 2: contentInfo
                "\"FROM_USER\" TEXT," + // 3: fromUser
                "\"NEWS_CONTENT\" TEXT," + // 4: newsContent
                "\"ME_CONTENT\" TEXT," + // 5: meContent
                "\"TITLE\" TEXT," + // 6: title
                "\"TASK_ID\" INTEGER," + // 7: taskId
                "\"MESSAGE_TIME\" INTEGER NOT NULL ," + // 8: messageTime
                "\"MESSAGE_TYPE\" INTEGER NOT NULL ," + // 9: messageType
                "\"TO_USER_ID\" INTEGER NOT NULL ," + // 10: toUserId
                "\"NEWS_TYPE\" INTEGER NOT NULL ," + // 11: newsType
                "\"SMALL_TYPE\" INTEGER NOT NULL ," + // 12: smallType
                "\"HAS_READ\" INTEGER NOT NULL ," + // 13: hasRead
                "\"IS_ME\" INTEGER NOT NULL ," + // 14: isMe
                "\"IS_ALARM\" INTEGER NOT NULL ," + // 15: isAlarm
                "\"IS_WORK\" INTEGER NOT NULL ," + // 16: isWork
                "\"IS_ENTERPRISE\" INTEGER NOT NULL ," + // 17: isEnterprise
                "\"CURRENT_USER_ID\" INTEGER NOT NULL );"); // 18: currentUserId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"file_news\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NewsBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String tip = entity.getTip();
        if (tip != null) {
            stmt.bindString(2, tip);
        }
 
        String contentInfo = entity.getContentInfo();
        if (contentInfo != null) {
            stmt.bindString(3, contentInfo);
        }
 
        String fromUser = entity.getFromUser();
        if (fromUser != null) {
            stmt.bindString(4, fromUser);
        }
 
        String newsContent = entity.getNewsContent();
        if (newsContent != null) {
            stmt.bindString(5, newsContent);
        }
 
        String meContent = entity.getMeContent();
        if (meContent != null) {
            stmt.bindString(6, meContent);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        Long taskId = entity.getTaskId();
        if (taskId != null) {
            stmt.bindLong(8, taskId);
        }
        stmt.bindLong(9, entity.getMessageTime());
        stmt.bindLong(10, entity.getMessageType());
        stmt.bindLong(11, entity.getToUserId());
        stmt.bindLong(12, entity.getNewsType());
        stmt.bindLong(13, entity.getSmallType());
        stmt.bindLong(14, entity.getHasRead() ? 1L: 0L);
        stmt.bindLong(15, entity.getIsMe() ? 1L: 0L);
        stmt.bindLong(16, entity.getIsAlarm() ? 1L: 0L);
        stmt.bindLong(17, entity.getIsWork() ? 1L: 0L);
        stmt.bindLong(18, entity.getIsEnterprise() ? 1L: 0L);
        stmt.bindLong(19, entity.getCurrentUserId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NewsBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String tip = entity.getTip();
        if (tip != null) {
            stmt.bindString(2, tip);
        }
 
        String contentInfo = entity.getContentInfo();
        if (contentInfo != null) {
            stmt.bindString(3, contentInfo);
        }
 
        String fromUser = entity.getFromUser();
        if (fromUser != null) {
            stmt.bindString(4, fromUser);
        }
 
        String newsContent = entity.getNewsContent();
        if (newsContent != null) {
            stmt.bindString(5, newsContent);
        }
 
        String meContent = entity.getMeContent();
        if (meContent != null) {
            stmt.bindString(6, meContent);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        Long taskId = entity.getTaskId();
        if (taskId != null) {
            stmt.bindLong(8, taskId);
        }
        stmt.bindLong(9, entity.getMessageTime());
        stmt.bindLong(10, entity.getMessageType());
        stmt.bindLong(11, entity.getToUserId());
        stmt.bindLong(12, entity.getNewsType());
        stmt.bindLong(13, entity.getSmallType());
        stmt.bindLong(14, entity.getHasRead() ? 1L: 0L);
        stmt.bindLong(15, entity.getIsMe() ? 1L: 0L);
        stmt.bindLong(16, entity.getIsAlarm() ? 1L: 0L);
        stmt.bindLong(17, entity.getIsWork() ? 1L: 0L);
        stmt.bindLong(18, entity.getIsEnterprise() ? 1L: 0L);
        stmt.bindLong(19, entity.getCurrentUserId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NewsBean readEntity(Cursor cursor, int offset) {
        NewsBean entity = new NewsBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tip
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // contentInfo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fromUser
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // newsContent
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // meContent
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // title
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // taskId
            cursor.getLong(offset + 8), // messageTime
            cursor.getInt(offset + 9), // messageType
            cursor.getInt(offset + 10), // toUserId
            cursor.getInt(offset + 11), // newsType
            cursor.getInt(offset + 12), // smallType
            cursor.getShort(offset + 13) != 0, // hasRead
            cursor.getShort(offset + 14) != 0, // isMe
            cursor.getShort(offset + 15) != 0, // isAlarm
            cursor.getShort(offset + 16) != 0, // isWork
            cursor.getShort(offset + 17) != 0, // isEnterprise
            cursor.getInt(offset + 18) // currentUserId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NewsBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTip(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContentInfo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFromUser(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNewsContent(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMeContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTitle(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTaskId(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setMessageTime(cursor.getLong(offset + 8));
        entity.setMessageType(cursor.getInt(offset + 9));
        entity.setToUserId(cursor.getInt(offset + 10));
        entity.setNewsType(cursor.getInt(offset + 11));
        entity.setSmallType(cursor.getInt(offset + 12));
        entity.setHasRead(cursor.getShort(offset + 13) != 0);
        entity.setIsMe(cursor.getShort(offset + 14) != 0);
        entity.setIsAlarm(cursor.getShort(offset + 15) != 0);
        entity.setIsWork(cursor.getShort(offset + 16) != 0);
        entity.setIsEnterprise(cursor.getShort(offset + 17) != 0);
        entity.setCurrentUserId(cursor.getInt(offset + 18));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NewsBean entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NewsBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NewsBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
