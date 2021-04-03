package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 创建设备数据库
 * Created by zhangan on 2017/9/22.
 */
@Entity(nameInDb = "create_equipment")
public class CreateEquipmentDb implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private Long equipmentId;//设备id
    private String equipmentName;//设备名称
    private int equipmentTypeId;//设备类型id
    private int voltageGradesId;//电压等级id
    private String equipmentNum;//设备位号
    private String productNum;//设备出厂编号
    private long createTime;//创建时间
    private boolean needUpload;//是否需要上传
    private long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getEquipmentId() {
        return this.equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getEquipmentTypeId() {
        return this.equipmentTypeId;
    }

    public void setEquipmentTypeId(int equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public String getEquipmentNum() {
        return this.equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getProductNum() {
        return this.productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
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

    public int getVoltageGradesId() {
        return this.voltageGradesId;
    }

    public void setVoltageGradesId(int voltageGradesId) {
        this.voltageGradesId = voltageGradesId;
    }

    public boolean isNeedUpload() {
        return needUpload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.equipmentId);
        dest.writeString(this.equipmentName);
        dest.writeInt(this.equipmentTypeId);
        dest.writeInt(this.voltageGradesId);
        dest.writeString(this.equipmentNum);
        dest.writeString(this.productNum);
        dest.writeLong(this.createTime);
        dest.writeByte(this.needUpload ? (byte) 1 : (byte) 0);
        dest.writeLong(this.currentUserId);
    }

    public long getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public CreateEquipmentDb() {
    }

    protected CreateEquipmentDb(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.equipmentId = (Long) in.readValue(Long.class.getClassLoader());
        this.equipmentName = in.readString();
        this.equipmentTypeId = in.readInt();
        this.voltageGradesId = in.readInt();
        this.equipmentNum = in.readString();
        this.productNum = in.readString();
        this.createTime = in.readLong();
        this.needUpload = in.readByte() != 0;
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 1167348038)
    public CreateEquipmentDb(Long _id, Long equipmentId, String equipmentName, int equipmentTypeId,
            int voltageGradesId, String equipmentNum, String productNum, long createTime,
            boolean needUpload, long currentUserId) {
        this._id = _id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentTypeId = equipmentTypeId;
        this.voltageGradesId = voltageGradesId;
        this.equipmentNum = equipmentNum;
        this.productNum = productNum;
        this.createTime = createTime;
        this.needUpload = needUpload;
        this.currentUserId = currentUserId;
    }

    public static final Creator<CreateEquipmentDb> CREATOR = new Creator<CreateEquipmentDb>() {
        @Override
        public CreateEquipmentDb createFromParcel(Parcel source) {
            return new CreateEquipmentDb(source);
        }

        @Override
        public CreateEquipmentDb[] newArray(int size) {
            return new CreateEquipmentDb[size];
        }
    };
}
