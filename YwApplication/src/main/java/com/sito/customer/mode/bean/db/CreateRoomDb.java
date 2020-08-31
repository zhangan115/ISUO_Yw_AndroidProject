package com.sito.customer.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.app.CustomerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创建区域数据库
 * Created by zhangan on 2017/9/22.
 */
@Entity(nameInDb = "create_room")
public class CreateRoomDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private Long roomId;//区域id
    private String roomName;//区域名称
    private int roomType;//区域类型
    private long createTime;//创建时间
    private boolean needUpload;//是否需要上传
    private long currentUserId = CustomerApp.getInstance().getCurrentUser().getUserId();

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isNeedUpload() {
        return needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.roomId);
        dest.writeString(this.roomName);
        dest.writeInt(this.roomType);
        dest.writeLong(this.createTime);
        dest.writeByte(this.needUpload ? (byte) 1 : (byte) 0);
        dest.writeLong(this.currentUserId);
    }

    public boolean getNeedUpload() {
        return this.needUpload;
    }

    public CreateRoomDb() {
    }

    protected CreateRoomDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = (Long) in.readValue(Long.class.getClassLoader());
        this.roomName = in.readString();
        this.roomType = in.readInt();
        this.createTime = in.readLong();
        this.needUpload = in.readByte() != 0;
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 1728823395)
    public CreateRoomDb(Long _id, Long roomId, String roomName, int roomType, long createTime, boolean needUpload,
            long currentUserId) {
        this._id = _id;
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.createTime = createTime;
        this.needUpload = needUpload;
        this.currentUserId = currentUserId;
    }

    public static final Parcelable.Creator<CreateRoomDb> CREATOR = new Parcelable.Creator<CreateRoomDb>() {
        @Override
        public CreateRoomDb createFromParcel(Parcel source) {
            return new CreateRoomDb(source);
        }

        @Override
        public CreateRoomDb[] newArray(int size) {
            return new CreateRoomDb[size];
        }
    };
}
