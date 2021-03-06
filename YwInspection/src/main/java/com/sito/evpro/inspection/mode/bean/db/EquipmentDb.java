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
    private long userId = InspectionApp.getInstance().getCurrentUser().getUserId();


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

    public long getEquipmentId() {
        return this.equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public boolean getAlarmState() {
        return this.alarmState;
    }

    public void setAlarmState(boolean alarmState) {
        this.alarmState = alarmState;
    }

    public boolean getUploadState() {
        return this.uploadState;
    }

    public void setUploadState(boolean uploadState) {
        this.uploadState = uploadState;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean getCanUpload() {
        return this.canUpload;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload;
    }

    public EquipmentDb() {
    }

    @Generated(hash = 9810235)
    public EquipmentDb(Long _id, long taskId, long roomId, long equipmentId,
            String equipmentName, boolean alarmState, boolean uploadState, boolean canUpload,
            long userId) {
        this._id = _id;
        this.taskId = taskId;
        this.roomId = roomId;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.alarmState = alarmState;
        this.uploadState = uploadState;
        this.canUpload = canUpload;
        this.userId = userId;
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
        dest.writeLong(this.userId);
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
        this.userId = in.readLong();
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
