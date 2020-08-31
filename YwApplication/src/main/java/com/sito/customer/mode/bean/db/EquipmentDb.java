package com.sito.customer.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.app.CustomerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 设备
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "equipment")
public class EquipmentDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private long taskId; //任务id
    private long roomId;//位置id
    private long equipmentId;//设备id
    private String equipmentName;
    private boolean alarmState;//是否异常
    private boolean uploadState;//是否上传
    private boolean canUpload;//是否可以上传
    private long currentUserId = CustomerApp.getInstance().getCurrentUser().getUserId();

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

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public boolean isAlarmState() {
        return alarmState;
    }

    public void setAlarmState(boolean alarmState) {
        this.alarmState = alarmState;
    }

    public boolean isUploadState() {
        return uploadState;
    }

    public void setUploadState(boolean uploadState) {
        this.uploadState = uploadState;
    }

    public boolean isCanUpload() {
        return canUpload;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload;
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
        dest.writeLong(this.equipmentId);
        dest.writeString(this.equipmentName);
        dest.writeByte(this.alarmState ? (byte) 1 : (byte) 0);
        dest.writeByte(this.uploadState ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canUpload ? (byte) 1 : (byte) 0);
        dest.writeLong(this.currentUserId);
    }

    public boolean getAlarmState() {
        return this.alarmState;
    }

    public boolean getUploadState() {
        return this.uploadState;
    }

    public boolean getCanUpload() {
        return this.canUpload;
    }

    public EquipmentDb() {
    }

    protected EquipmentDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.taskId = in.readLong();
        this.roomId = in.readLong();
        this.equipmentId = in.readLong();
        this.equipmentName = in.readString();
        this.alarmState = in.readByte() != 0;
        this.uploadState = in.readByte() != 0;
        this.canUpload = in.readByte() != 0;
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 911097223)
    public EquipmentDb(Long _id, long taskId, long roomId, long equipmentId,
                       String equipmentName, boolean alarmState, boolean uploadState, boolean canUpload,
                       long currentUserId) {
        this._id = _id;
        this.taskId = taskId;
        this.roomId = roomId;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.alarmState = alarmState;
        this.uploadState = uploadState;
        this.canUpload = canUpload;
        this.currentUserId = currentUserId;
    }

    public static final Creator<EquipmentDb> CREATOR = new Creator<EquipmentDb>() {
        @Override
        public EquipmentDb createFromParcel(Parcel source) {
            return new EquipmentDb(source);
        }

        @Override
        public EquipmentDb[] newArray(int size) {
            return new EquipmentDb[size];
        }
    };
}
