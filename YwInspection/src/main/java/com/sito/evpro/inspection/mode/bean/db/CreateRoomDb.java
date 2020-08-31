package com.sito.evpro.inspection.mode.bean.db;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创建区域数据库
 * Created by zhangan on 2017/9/22.
 */
@Entity(nameInDb = "create_room")
public class CreateRoomDb {
    @Id(autoincrement = true)
    private Long _id;
    private Long roomId;//区域id
    private String roomName;//区域名称
    private int roomType;//区域类型
    private long createTime;//创建时间
    private boolean needUpload;//是否需要上传
    private int userId = InspectionApp.getInstance().getCurrentUser().getUserId();

    @Generated(hash = 278822889)
    public CreateRoomDb(Long _id, Long roomId, String roomName, int roomType,
            long createTime, boolean needUpload, int userId) {
        this._id = _id;
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.createTime = createTime;
        this.needUpload = needUpload;
        this.userId = userId;
    }

    @Generated(hash = 644387361)
    public CreateRoomDb() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getRoomId() {
        return this.roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public int getRoomType() {
        return this.roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean getNeedUpload() {
        return this.needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
