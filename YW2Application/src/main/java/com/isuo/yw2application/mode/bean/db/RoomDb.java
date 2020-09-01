package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;


import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 配电室列表
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "room")
public class RoomDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private long taskId; //任务id
    private long roomId;//位置id
    private long taskRoomId;//本次巡检任务配电室id
    private long lastSaveTime;//最后保存时间
    private long takePhotoPosition = -1;
    private long startTime;
    private long endTime;
    private String roomName;
    private String photoUrl;
    private String uploadPhotoUrl;
    private int taskState;//状态 0 未开始，1进行中，2已完成
    private int checkCount;
    private long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getTaskRoomId() {
        return taskRoomId;
    }

    public void setTaskRoomId(long taskRoomId) {
        this.taskRoomId = taskRoomId;
    }

    public long getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public long getTakePhotoPosition() {
        return takePhotoPosition;
    }

    public void setTakePhotoPosition(long takePhotoPosition) {
        this.takePhotoPosition = takePhotoPosition;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUploadPhotoUrl() {
        return uploadPhotoUrl;
    }

    public void setUploadPhotoUrl(String uploadPhotoUrl) {
        this.uploadPhotoUrl = uploadPhotoUrl;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
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
        dest.writeLong(this.taskId);
        dest.writeLong(this.roomId);
        dest.writeLong(this.taskRoomId);
        dest.writeLong(this.lastSaveTime);
        dest.writeLong(this.takePhotoPosition);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.roomName);
        dest.writeString(this.photoUrl);
        dest.writeString(this.uploadPhotoUrl);
        dest.writeInt(this.taskState);
        dest.writeInt(this.checkCount);
        dest.writeLong(this.currentUserId);
    }

    public RoomDb() {
    }

    protected RoomDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.taskId = in.readLong();
        this.roomId = in.readLong();
        this.taskRoomId = in.readLong();
        this.lastSaveTime = in.readLong();
        this.takePhotoPosition = in.readLong();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.roomName = in.readString();
        this.photoUrl = in.readString();
        this.uploadPhotoUrl = in.readString();
        this.taskState = in.readInt();
        this.checkCount = in.readInt();
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 1698537001)
    public RoomDb(Long _id, long taskId, long roomId, long taskRoomId, long lastSaveTime,
            long takePhotoPosition, long startTime, long endTime, String roomName,
            String photoUrl, String uploadPhotoUrl, int taskState, int checkCount,
            long currentUserId) {
        this._id = _id;
        this.taskId = taskId;
        this.roomId = roomId;
        this.taskRoomId = taskRoomId;
        this.lastSaveTime = lastSaveTime;
        this.takePhotoPosition = takePhotoPosition;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomName = roomName;
        this.photoUrl = photoUrl;
        this.uploadPhotoUrl = uploadPhotoUrl;
        this.taskState = taskState;
        this.checkCount = checkCount;
        this.currentUserId = currentUserId;
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
