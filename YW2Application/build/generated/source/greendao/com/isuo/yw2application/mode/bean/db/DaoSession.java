package com.isuo.yw2application.mode.bean.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.isuo.yw2application.mode.bean.db.Voice;
import com.isuo.yw2application.mode.bean.db.Image;
import com.isuo.yw2application.mode.bean.db.TaskDb;
import com.isuo.yw2application.mode.bean.db.ShareDataDb;
import com.isuo.yw2application.mode.bean.db.UserInfo;
import com.isuo.yw2application.mode.bean.db.NewsBean;
import com.isuo.yw2application.mode.bean.db.ReadNewsBean;
import com.isuo.yw2application.mode.bean.db.CreateEquipmentDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDb;
import com.isuo.yw2application.mode.bean.db.RoomDb;
import com.isuo.yw2application.mode.bean.db.CreateRoomDb;
import com.isuo.yw2application.mode.bean.db.EquipmentDb;

import com.isuo.yw2application.mode.bean.db.VoiceDao;
import com.isuo.yw2application.mode.bean.db.ImageDao;
import com.isuo.yw2application.mode.bean.db.TaskDbDao;
import com.isuo.yw2application.mode.bean.db.ShareDataDbDao;
import com.isuo.yw2application.mode.bean.db.UserInfoDao;
import com.isuo.yw2application.mode.bean.db.NewsBeanDao;
import com.isuo.yw2application.mode.bean.db.ReadNewsBeanDao;
import com.isuo.yw2application.mode.bean.db.CreateEquipmentDbDao;
import com.isuo.yw2application.mode.bean.db.EquipmentDataDbDao;
import com.isuo.yw2application.mode.bean.db.RoomDbDao;
import com.isuo.yw2application.mode.bean.db.CreateRoomDbDao;
import com.isuo.yw2application.mode.bean.db.EquipmentDbDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig voiceDaoConfig;
    private final DaoConfig imageDaoConfig;
    private final DaoConfig taskDbDaoConfig;
    private final DaoConfig shareDataDbDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig newsBeanDaoConfig;
    private final DaoConfig readNewsBeanDaoConfig;
    private final DaoConfig createEquipmentDbDaoConfig;
    private final DaoConfig equipmentDataDbDaoConfig;
    private final DaoConfig roomDbDaoConfig;
    private final DaoConfig createRoomDbDaoConfig;
    private final DaoConfig equipmentDbDaoConfig;

    private final VoiceDao voiceDao;
    private final ImageDao imageDao;
    private final TaskDbDao taskDbDao;
    private final ShareDataDbDao shareDataDbDao;
    private final UserInfoDao userInfoDao;
    private final NewsBeanDao newsBeanDao;
    private final ReadNewsBeanDao readNewsBeanDao;
    private final CreateEquipmentDbDao createEquipmentDbDao;
    private final EquipmentDataDbDao equipmentDataDbDao;
    private final RoomDbDao roomDbDao;
    private final CreateRoomDbDao createRoomDbDao;
    private final EquipmentDbDao equipmentDbDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        voiceDaoConfig = daoConfigMap.get(VoiceDao.class).clone();
        voiceDaoConfig.initIdentityScope(type);

        imageDaoConfig = daoConfigMap.get(ImageDao.class).clone();
        imageDaoConfig.initIdentityScope(type);

        taskDbDaoConfig = daoConfigMap.get(TaskDbDao.class).clone();
        taskDbDaoConfig.initIdentityScope(type);

        shareDataDbDaoConfig = daoConfigMap.get(ShareDataDbDao.class).clone();
        shareDataDbDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        newsBeanDaoConfig = daoConfigMap.get(NewsBeanDao.class).clone();
        newsBeanDaoConfig.initIdentityScope(type);

        readNewsBeanDaoConfig = daoConfigMap.get(ReadNewsBeanDao.class).clone();
        readNewsBeanDaoConfig.initIdentityScope(type);

        createEquipmentDbDaoConfig = daoConfigMap.get(CreateEquipmentDbDao.class).clone();
        createEquipmentDbDaoConfig.initIdentityScope(type);

        equipmentDataDbDaoConfig = daoConfigMap.get(EquipmentDataDbDao.class).clone();
        equipmentDataDbDaoConfig.initIdentityScope(type);

        roomDbDaoConfig = daoConfigMap.get(RoomDbDao.class).clone();
        roomDbDaoConfig.initIdentityScope(type);

        createRoomDbDaoConfig = daoConfigMap.get(CreateRoomDbDao.class).clone();
        createRoomDbDaoConfig.initIdentityScope(type);

        equipmentDbDaoConfig = daoConfigMap.get(EquipmentDbDao.class).clone();
        equipmentDbDaoConfig.initIdentityScope(type);

        voiceDao = new VoiceDao(voiceDaoConfig, this);
        imageDao = new ImageDao(imageDaoConfig, this);
        taskDbDao = new TaskDbDao(taskDbDaoConfig, this);
        shareDataDbDao = new ShareDataDbDao(shareDataDbDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        newsBeanDao = new NewsBeanDao(newsBeanDaoConfig, this);
        readNewsBeanDao = new ReadNewsBeanDao(readNewsBeanDaoConfig, this);
        createEquipmentDbDao = new CreateEquipmentDbDao(createEquipmentDbDaoConfig, this);
        equipmentDataDbDao = new EquipmentDataDbDao(equipmentDataDbDaoConfig, this);
        roomDbDao = new RoomDbDao(roomDbDaoConfig, this);
        createRoomDbDao = new CreateRoomDbDao(createRoomDbDaoConfig, this);
        equipmentDbDao = new EquipmentDbDao(equipmentDbDaoConfig, this);

        registerDao(Voice.class, voiceDao);
        registerDao(Image.class, imageDao);
        registerDao(TaskDb.class, taskDbDao);
        registerDao(ShareDataDb.class, shareDataDbDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(NewsBean.class, newsBeanDao);
        registerDao(ReadNewsBean.class, readNewsBeanDao);
        registerDao(CreateEquipmentDb.class, createEquipmentDbDao);
        registerDao(EquipmentDataDb.class, equipmentDataDbDao);
        registerDao(RoomDb.class, roomDbDao);
        registerDao(CreateRoomDb.class, createRoomDbDao);
        registerDao(EquipmentDb.class, equipmentDbDao);
    }
    
    public void clear() {
        voiceDaoConfig.clearIdentityScope();
        imageDaoConfig.clearIdentityScope();
        taskDbDaoConfig.clearIdentityScope();
        shareDataDbDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        newsBeanDaoConfig.clearIdentityScope();
        readNewsBeanDaoConfig.clearIdentityScope();
        createEquipmentDbDaoConfig.clearIdentityScope();
        equipmentDataDbDaoConfig.clearIdentityScope();
        roomDbDaoConfig.clearIdentityScope();
        createRoomDbDaoConfig.clearIdentityScope();
        equipmentDbDaoConfig.clearIdentityScope();
    }

    public VoiceDao getVoiceDao() {
        return voiceDao;
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

    public TaskDbDao getTaskDbDao() {
        return taskDbDao;
    }

    public ShareDataDbDao getShareDataDbDao() {
        return shareDataDbDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public NewsBeanDao getNewsBeanDao() {
        return newsBeanDao;
    }

    public ReadNewsBeanDao getReadNewsBeanDao() {
        return readNewsBeanDao;
    }

    public CreateEquipmentDbDao getCreateEquipmentDbDao() {
        return createEquipmentDbDao;
    }

    public EquipmentDataDbDao getEquipmentDataDbDao() {
        return equipmentDataDbDao;
    }

    public RoomDbDao getRoomDbDao() {
        return roomDbDao;
    }

    public CreateRoomDbDao getCreateRoomDbDao() {
        return createRoomDbDao;
    }

    public EquipmentDbDao getEquipmentDbDao() {
        return equipmentDbDao;
    }

}
