package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 巡检数据
 * Created by zhangan on 2017-07-10.
 */
@Entity(nameInDb = "equipment_data")
public class EquipmentDataDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private String value;//值
    private String chooseInspectionName;
    private String localPhoto;

    private int type;//类型
    private long taskId;
    private long roomId;
    private long equipmentId;
    private long dataItemId;
    private long inspectionId;
    private int isShareValue;
    private int isRequired;//1 必填，0 非必填

    private boolean isUpload;
    private long userId;
    private long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getChooseInspectionName() {
        return chooseInspectionName;
    }

    public void setChooseInspectionName(String chooseInspectionName) {
        this.chooseInspectionName = chooseInspectionName;
    }

    public long getDataItemId() {
        return dataItemId;
    }

    public void setDataItemId(long dataItemId) {
        this.dataItemId = dataItemId;
    }

    public String getLocalPhoto() {
        return localPhoto;
    }

    public void setLocalPhoto(String localPhoto) {
        this.localPhoto = localPhoto;
    }

    public long getInspectionId() {
        return this.inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public int getIsShareValue() {
        return isShareValue;
    }

    public void setIsShareValue(int isShareValue) {
        this.isShareValue = isShareValue;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public EquipmentDataDb() {
    }
    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.value);
        dest.writeInt(this.type);
        dest.writeLong(this.taskId);
        dest.writeLong(this.roomId);
        dest.writeLong(this.equipmentId);
        dest.writeString(this.chooseInspectionName);
        dest.writeLong(this.dataItemId);
        dest.writeString(this.localPhoto);
        dest.writeLong(this.inspectionId);
        dest.writeInt(this.isShareValue);
        dest.writeInt(this.isRequired);
        dest.writeLong(this.userId);
        dest.writeLong(this.currentUserId);
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    protected EquipmentDataDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.value = in.readString();
        this.type = in.readInt();
        this.taskId = in.readLong();
        this.roomId = in.readLong();
        this.equipmentId = in.readLong();
        this.chooseInspectionName = in.readString();
        this.dataItemId = in.readLong();
        this.localPhoto = in.readString();
        this.inspectionId = in.readLong();
        this.isShareValue = in.readInt();
        this.isRequired = in.readInt();
        this.userId =in.readInt();
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 1565587596)
    public EquipmentDataDb(Long _id, String value, String chooseInspectionName,
            String localPhoto, int type, long taskId, long roomId, long equipmentId,
            long dataItemId, long inspectionId, int isShareValue, int isRequired,
            boolean isUpload, long userId, long currentUserId) {
        this._id = _id;
        this.value = value;
        this.chooseInspectionName = chooseInspectionName;
        this.localPhoto = localPhoto;
        this.type = type;
        this.taskId = taskId;
        this.roomId = roomId;
        this.equipmentId = equipmentId;
        this.dataItemId = dataItemId;
        this.inspectionId = inspectionId;
        this.isShareValue = isShareValue;
        this.isRequired = isRequired;
        this.isUpload = isUpload;
        this.userId = userId;
        this.currentUserId = currentUserId;
    }

    public static final Creator<EquipmentDataDb> CREATOR = new Creator<EquipmentDataDb>() {
        @Override
        public EquipmentDataDb createFromParcel(Parcel source) {
            return new EquipmentDataDb(source);
        }

        @Override
        public EquipmentDataDb[] newArray(int size) {
            return new EquipmentDataDb[size];
        }
    };
}
