package com.isuo.yw2application.mode.fire;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


/**
 * 设备
 * Created by zhangan on 2017-07-10.
 */

public class FireBean implements Parcelable {

    private long createTime;
    private int deleteState;
    private long equipmentId;
    private String manufacturer;//生产厂
    private String equipmentName;
    private String equipmentNumber;
    private String equipmentRemark;
    private long manufactureTime;//生产时间
    private long nearTime;//提醒时间
    private long expireTime;//过期时间
    private String supplier;//供应商

    private String room1;//所属配电
    private String room2;//所属配电
    private String room3;//所属配电

    private String equipmentSn;//编号
    private int count;//安装数量
    private int equipmentState;//触发状态 0 正常 1 触发
    private int workingState;//工作状态 0 线上 1 线下

    private String itemNumber;//出厂型号
    private String equipmentFsn;//设备出厂编号

    public FireBean(){

    }

    FireBean(Parcel in) {
        createTime = in.readLong();
        deleteState = in.readInt();
        equipmentId = in.readLong();
        manufacturer = in.readString();
        equipmentName = in.readString();
        equipmentNumber = in.readString();
        equipmentRemark = in.readString();
        manufactureTime = in.readLong();
        nearTime = in.readLong();
        expireTime = in.readLong();
        supplier = in.readString();
        room1 = in.readString();
        room2 = in.readString();
        room3 = in.readString();
        equipmentSn = in.readString();
        count = in.readInt();
        equipmentState = in.readInt();
        workingState = in.readInt();
        itemNumber = in.readString();
        equipmentFsn = in.readString();
    }

    public static final Creator<FireBean> CREATOR = new Creator<FireBean>() {
        @Override
        public FireBean createFromParcel(Parcel in) {
            return new FireBean(in);
        }

        @Override
        public FireBean[] newArray(int size) {
            return new FireBean[size];
        }
    };

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getEquipmentRemark() {
        return equipmentRemark;
    }

    public void setEquipmentRemark(String equipmentRemark) {
        this.equipmentRemark = equipmentRemark;
    }

    public long getManufactureTime() {
        return manufactureTime;
    }

    public void setManufactureTime(long manufactureTime) {
        this.manufactureTime = manufactureTime;
    }

    public long getNearTime() {
        return nearTime;
    }

    public void setNearTime(long nearTime) {
        this.nearTime = nearTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRoom1() {
        return room1;
    }

    public void setRoom1(String room1) {
        this.room1 = room1;
    }

    public String getRoom2() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2 = room2;
    }

    public String getRoom3() {
        return room3;
    }

    public void setRoom3(String room3) {
        this.room3 = room3;
    }

    public String getEquipmentSn() {
        return equipmentSn;
    }

    public void setEquipmentSn(String equipmentSn) {
        this.equipmentSn = equipmentSn;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEquipmentState() {
        return equipmentState;
    }

    public void setEquipmentState(int equipmentState) {
        this.equipmentState = equipmentState;
    }

    public int getWorkingState() {
        return workingState;
    }

    public void setWorkingState(int workingState) {
        this.workingState = workingState;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getEquipmentFsn() {
        return equipmentFsn;
    }

    public void setEquipmentFsn(String equipmentFsn) {
        this.equipmentFsn = equipmentFsn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeInt(deleteState);
        dest.writeLong(equipmentId);
        dest.writeString(manufacturer);
        dest.writeString(equipmentName);
        dest.writeString(equipmentNumber);
        dest.writeString(equipmentRemark);
        dest.writeLong(manufactureTime);
        dest.writeLong(nearTime);
        dest.writeLong(expireTime);
        dest.writeString(supplier);
        dest.writeString(room1);
        dest.writeString(room2);
        dest.writeString(room3);
        dest.writeString(equipmentSn);
        dest.writeInt(count);
        dest.writeInt(equipmentState);
        dest.writeInt(workingState);
        dest.writeString(itemNumber);
        dest.writeString(equipmentFsn);
    }
}
