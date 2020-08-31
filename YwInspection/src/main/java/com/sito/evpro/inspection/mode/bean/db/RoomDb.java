package com.sito.evpro.inspection.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "room")
public class RoomDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private long taskId; //任务id
    private long roomId;//位置id
    private int state;//状态 0 未开始，1进行中，2已完成
    private long lastSaveTime;//最后保存时间
    private long takePhotoPosition = -1;
    private String photoUrl;
    private String uploadPhotoUrl;
    private int checkCount;
    private long userId = InspectionApp.getInstance().getCurrentUser().getUserId();

    @Generated(hash = 233564109)
    public RoomDb(Long _id, long taskId, long roomId, int state, long lastSaveTime,
            long takePhotoPosition, String photoUrl, String uploadPhotoUrl, int checkCount,
            long userId) {
        this._id = _id;
        this.taskId = taskId;
        this.roomId = roomId;
        this.state = state;
        this.lastSaveTime = lastSaveTime;
        this.takePhotoPosition = takePhotoPosition;
        this.photoUrl = photoUrl;
        this.uploadPhotoUrl = uploadPhotoUrl;
        this.checkCount = checkCount;
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Generated(hash = 1777354796)
    public RoomDb() {

    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getRoomId() {
        return this.roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastSaveTime() {
        return this.lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTakePhotoPosition() {
        return takePhotoPosition;
    }

    public void setTakePhotoPosition(long takePhotoPosition) {
        this.takePhotoPosition = takePhotoPosition;
    }

    public String getUploadPhotoUrl() {
        return this.uploadPhotoUrl;
    }

    public void setUploadPhotoUrl(String uploadPhotoUrl) {
        this.uploadPhotoUrl = uploadPhotoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeLong(this.taskId);
        dest.writeLong(this.roomId);
        dest.writeInt(this.state);
        dest.writeLong(this.lastSaveTime);
        dest.writeLong(this.takePhotoPosition);
        dest.writeString(this.photoUrl);
        dest.writeString(this.uploadPhotoUrl);
        dest.writeInt(this.checkCount);
        dest.writeLong(this.userId);
    }

    protected RoomDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.taskId = in.readLong();
        this.roomId = in.readLong();
        this.state = in.readInt();
        this.lastSaveTime = in.readLong();
        this.takePhotoPosition = in.readLong();
        this.photoUrl = in.readString();
        this.uploadPhotoUrl = in.readString();
        this.checkCount = in.readInt();
        this.userId = in.readLong();
    }

    public static final Creator<RoomDb> CREATOR = new Creator<RoomDb>() {
        @Override
        public RoomDb createFromParcel(Parcel source) {
            return new RoomDb(source);
        }

        @Override
        public RoomDb[] newArray(int size) {
            return new RoomDb[size];
        }
    };
}
