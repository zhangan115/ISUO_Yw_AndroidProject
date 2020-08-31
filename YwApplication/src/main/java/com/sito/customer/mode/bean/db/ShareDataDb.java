package com.sito.customer.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.app.CustomerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 共享数据
 * Created by zhangan on 2018/5/9.
 */
@Entity(nameInDb = "share_data")
public class ShareDataDb implements Parcelable {

    @Id
    private long inspectionId;
    private String value;
    private int type;//类型
    private long taskId;
    private long roomId;
    private long equipmentId;
    private String chooseInspectionName;
    private long dataItemId;
    private String localPhoto;
    private long currentUserId = CustomerApp.getInstance().getCurrentUser().getUserId();

    public static ShareDataDb getShareDataDb(EquipmentDataDb equipmentDataDb) {
        ShareDataDb shareDataDb = new ShareDataDb();
        shareDataDb.setInspectionId(equipmentDataDb.getInspectionId());
        shareDataDb.setChooseInspectionName(equipmentDataDb.getChooseInspectionName());
        shareDataDb.setDataItemId(equipmentDataDb.getDataItemId());
        shareDataDb.setEquipmentId(equipmentDataDb.getEquipmentId());
        shareDataDb.setLocalPhoto(equipmentDataDb.getLocalPhoto());
        shareDataDb.setValue(equipmentDataDb.getValue());
        shareDataDb.setTaskId(equipmentDataDb.getTaskId());
        shareDataDb.setType(equipmentDataDb.getType());
        shareDataDb.setRoomId(equipmentDataDb.getRoomId());
        return shareDataDb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.inspectionId);
        dest.writeString(this.value);
        dest.writeInt(this.type);
        dest.writeLong(this.taskId);
        dest.writeLong(this.roomId);
        dest.writeLong(this.equipmentId);
        dest.writeString(this.chooseInspectionName);
        dest.writeLong(this.dataItemId);
        dest.writeString(this.localPhoto);
        dest.writeLong(this.currentUserId);
    }

    public long getInspectionId() {
        return this.inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getChooseInspectionName() {
        return this.chooseInspectionName;
    }

    public void setChooseInspectionName(String chooseInspectionName) {
        this.chooseInspectionName = chooseInspectionName;
    }

    public long getDataItemId() {
        return this.dataItemId;
    }

    public void setDataItemId(long dataItemId) {
        this.dataItemId = dataItemId;
    }

    public String getLocalPhoto() {
        return this.localPhoto;
    }

    public void setLocalPhoto(String localPhoto) {
        this.localPhoto = localPhoto;
    }

    public long getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public ShareDataDb() {
    }

    protected ShareDataDb(Parcel in) {
        this.inspectionId = in.readLong();
        this.value = in.readString();
        this.type = in.readInt();
        this.taskId = in.readLong();
        this.roomId = in.readLong();
        this.equipmentId = in.readLong();
        this.chooseInspectionName = in.readString();
        this.dataItemId = in.readLong();
        this.localPhoto = in.readString();
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 414383705)
    public ShareDataDb(long inspectionId, String value, int type, long taskId, long roomId,
                       long equipmentId, String chooseInspectionName, long dataItemId, String localPhoto,
                       long currentUserId) {
        this.inspectionId = inspectionId;
        this.value = value;
        this.type = type;
        this.taskId = taskId;
        this.roomId = roomId;
        this.equipmentId = equipmentId;
        this.chooseInspectionName = chooseInspectionName;
        this.dataItemId = dataItemId;
        this.localPhoto = localPhoto;
        this.currentUserId = currentUserId;
    }

    public static final Creator<ShareDataDb> CREATOR = new Creator<ShareDataDb>() {
        @Override
        public ShareDataDb createFromParcel(Parcel source) {
            return new ShareDataDb(source);
        }

        @Override
        public ShareDataDb[] newArray(int size) {
            return new ShareDataDb[size];
        }
    };
}
