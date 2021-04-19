package com.isuo.yw2application.mode.fire;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


/**
 * 设备
 * Created by zhangan on 2017-07-10.
 */

public class FireBean implements Parcelable {

    private String roomName;//所属配电
    private String twoRegion;//二级
    private String threeRegion;//三级
    private String equipmentName;//设备名称
    private String equipmentNumber;//安装设备编号
    private String equipmentType;//出厂型号
    private int count;//安装数量
    private String companyNumber;//出厂编号
    private int workingState;//工作状态 0 线上 1 线下
    private int equipmentState;//触发状态 0 正常 1 触发
    private long manufactureTime;//生产时间
    private long remindTime;//提醒时间
    private long expireTime;//过期时间
    private String equipmentModel;//过期时间
    private String remark;
    private String equipmentPerson;

    public FireBean(){

    }

    protected FireBean(Parcel in) {
        roomName = in.readString();
        twoRegion = in.readString();
        threeRegion = in.readString();
        equipmentName = in.readString();
        equipmentNumber = in.readString();
        equipmentType = in.readString();
        count = in.readInt();
        companyNumber = in.readString();
        workingState = in.readInt();
        equipmentState = in.readInt();
        manufactureTime = in.readLong();
        remindTime = in.readLong();
        expireTime = in.readLong();
        remark = in.readString();
        equipmentPerson = in.readString();
        equipmentModel = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roomName);
        parcel.writeString(twoRegion);
        parcel.writeString(threeRegion);
        parcel.writeString(equipmentName);
        parcel.writeString(equipmentNumber);
        parcel.writeString(equipmentType);
        parcel.writeInt(count);
        parcel.writeString(companyNumber);
        parcel.writeInt(workingState);
        parcel.writeInt(equipmentState);
        parcel.writeLong(manufactureTime);
        parcel.writeLong(remindTime);
        parcel.writeLong(expireTime);
        parcel.writeString(remark);
        parcel.writeString(equipmentPerson);
        parcel.writeString(equipmentModel);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTwoRegion() {
        return twoRegion;
    }

    public void setTwoRegion(String twoRegion) {
        this.twoRegion = twoRegion;
    }

    public String getThreeRegion() {
        return threeRegion;
    }

    public void setThreeRegion(String threeRegion) {
        this.threeRegion = threeRegion;
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

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getWorkingState() {
        return workingState;
    }

    public void setWorkingState(int workingState) {
        this.workingState = workingState;
    }

    public int getEquipmentState() {
        return equipmentState;
    }

    public void setEquipmentState(int equipmentState) {
        this.equipmentState = equipmentState;
    }

    public long getManufactureTime() {
        return manufactureTime;
    }

    public void setManufactureTime(long manufactureTime) {
        this.manufactureTime = manufactureTime;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getNote() {
        return remark;
    }

    public void setNote(String note) {
        this.remark = note;
    }

    public String getEquipmentPerson() {
        return equipmentPerson;
    }

    public void setEquipmentPerson(String equipmentPerson) {
        this.equipmentPerson = equipmentPerson;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }
}
