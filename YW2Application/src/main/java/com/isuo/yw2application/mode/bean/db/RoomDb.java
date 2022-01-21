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
    private String dataItemName = null;
    private long startTime;
    private long endTime;
    private String roomName;
    private String photoUrl;
    private String uploadPhotoUrl;
    private int taskState;//状态 0 未开始，1进行中，2已完成
    private int checkCount;
    private boolean isUpload = false;
    private long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();

    protected RoomDb(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        taskId = in.readLong();
        roomId = in.readLong();
        taskRoomId = in.readLong();
        lastSaveTime = in.readLong();
        takePhotoPosition = in.readLong();
        dataItemName = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        roomName = in.readString();
        photoUrl = in.readString();
        uploadPhotoUrl = in.readString();
        taskState = in.readInt();
        checkCount = in.readInt();
        isUpload = in.readByte() != 0;
        currentUserId = in.readLong();
    }

    public static final Creator<RoomDb> CREATOR = new Creator<RoomDb>() {
        @Override
        public RoomDb createFromParcel(Parcel in) {
            return new RoomDb(in);
        }

        @Override
        public RoomDb[] newArray(int size) {
            return new RoomDb[size];
        }
    };

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

    public String getDataItemName() {
        return dataItemName;
    }

    public void setDataItemName(String dataItemName) {
        this.dataItemName = dataItemName;
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


    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public RoomDb() {
    }


    @Generated(hash = 1453413949)
    public RoomDb(Long _id, long taskId, long roomId, long taskRoomId, long lastSaveTime,
            long takePhotoPosition, String dataItemName, long startTime, long endTime,
            String roomName, String photoUrl, String uploadPhotoUrl, int taskState,
            int checkCount, boolean isUpload, long currentUserId) {
        this._id = _id;
        this.taskId = taskId;
        this.roomId = roomId;
        this.taskRoomId = taskRoomId;
        this.lastSaveTime = lastSaveTime;
        this.takePhotoPosition = takePhotoPosition;
        this.dataItemName = dataItemName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomName = roomName;
        this.photoUrl = photoUrl;
        this.uploadPhotoUrl = uploadPhotoUrl;
        this.taskState = taskState;
        this.checkCount = checkCount;
        this.isUpload = isUpload;
        this.currentUserId = currentUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(_id);
        }
        dest.writeLong(taskId);
        dest.writeLong(roomId);
        dest.writeLong(taskRoomId);
        dest.writeLong(lastSaveTime);
        dest.writeLong(takePhotoPosition);
        dest.writeString(dataItemName);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(roomName);
        dest.writeString(photoUrl);
        dest.writeString(uploadPhotoUrl);
        dest.writeInt(taskState);
        dest.writeInt(checkCount);
        dest.writeByte((byte) (isUpload ? 1 : 0));
        dest.writeLong(currentUserId);
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
