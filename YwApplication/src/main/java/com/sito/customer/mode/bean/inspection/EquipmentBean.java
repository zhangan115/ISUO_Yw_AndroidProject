package com.sito.customer.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;


import com.sito.customer.mode.bean.db.EquipmentDb;

import java.util.List;

/**
 * 设备信息
 * Created by zhangan on 2017-07-10.
 */

public class EquipmentBean implements Parcelable {
    private long createTime;
    private int deleteState;
    private long equipmentId;
    private String equipmentName;
    private String equipmentNumber;
    private String equipmentRemark;
    private long manufactureTime;
    private String manufacturer;//生产厂
    private String supplier;//供应商
    private String nameplatePicUrl;
    private String equipmentSn;
    private int voltageLevel;
    private int equipmentState;
    private int workingState;
    private long startTime;
    private long installTime;
    private String itemNumber;
    private String equipmentFsn;//设备出厂编号
    private EquipmentType equipmentType;
    private String equipmentAlias;
    private RoomBean room;//所属配电
    private EquipmentDb equipmentDb;//本地添加属性:设备录入的数据
    private List<ExpandListBean> expandList;
    private int isOnFocus;//是否已关注：0或null为未关注，1为审核中，2为已关注

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getNameplatePicUrl() {
        return nameplatePicUrl;
    }

    public void setNameplatePicUrl(String nameplatePicUrl) {
        this.nameplatePicUrl = nameplatePicUrl;
    }

    public String getEquipmentSn() {
        return equipmentSn;
    }

    public void setEquipmentSn(String equipmentSn) {
        this.equipmentSn = equipmentSn;
    }

    public int getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(int voltageLevel) {
        this.voltageLevel = voltageLevel;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(long installTime) {
        this.installTime = installTime;
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

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentAlias() {
        return equipmentAlias;
    }

    public void setEquipmentAlias(String equipmentAlias) {
        this.equipmentAlias = equipmentAlias;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public EquipmentDb getEquipmentDb() {
        return equipmentDb;
    }

    public void setEquipmentDb(EquipmentDb equipmentDb) {
        this.equipmentDb = equipmentDb;
    }

    public List<ExpandListBean> getExpandList() {
        return expandList;
    }

    public void setExpandList(List<ExpandListBean> expandList) {
        this.expandList = expandList;
    }

    public int getIsOnFocus() {
        return isOnFocus;
    }

    public void setIsOnFocus(int isOnFocus) {
        this.isOnFocus = isOnFocus;
    }

    public EquipmentBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.deleteState);
        dest.writeLong(this.equipmentId);
        dest.writeString(this.equipmentName);
        dest.writeString(this.equipmentNumber);
        dest.writeString(this.equipmentRemark);
        dest.writeLong(this.manufactureTime);
        dest.writeString(this.manufacturer);
        dest.writeString(this.supplier);
        dest.writeString(this.nameplatePicUrl);
        dest.writeString(this.equipmentSn);
        dest.writeInt(this.voltageLevel);
        dest.writeInt(this.equipmentState);
        dest.writeInt(this.workingState);
        dest.writeLong(this.startTime);
        dest.writeLong(this.installTime);
        dest.writeString(this.itemNumber);
        dest.writeString(this.equipmentFsn);
        dest.writeParcelable(this.equipmentType, flags);
        dest.writeString(this.equipmentAlias);
        dest.writeParcelable(this.room, flags);
        dest.writeParcelable(this.equipmentDb, flags);
        dest.writeTypedList(this.expandList);
        dest.writeInt(this.isOnFocus);
    }

    protected EquipmentBean(Parcel in) {
        this.createTime = in.readLong();
        this.deleteState = in.readInt();
        this.equipmentId = in.readLong();
        this.equipmentName = in.readString();
        this.equipmentNumber = in.readString();
        this.equipmentRemark = in.readString();
        this.manufactureTime = in.readLong();
        this.manufacturer = in.readString();
        this.supplier = in.readString();
        this.nameplatePicUrl = in.readString();
        this.equipmentSn = in.readString();
        this.voltageLevel = in.readInt();
        this.equipmentState = in.readInt();
        this.workingState = in.readInt();
        this.startTime = in.readLong();
        this.installTime = in.readLong();
        this.itemNumber = in.readString();
        this.equipmentFsn = in.readString();
        this.equipmentType = in.readParcelable(EquipmentType.class.getClassLoader());
        this.equipmentAlias = in.readString();
        this.room = in.readParcelable(RoomBean.class.getClassLoader());
        this.equipmentDb = in.readParcelable(EquipmentDb.class.getClassLoader());
        this.expandList = in.createTypedArrayList(ExpandListBean.CREATOR);
        this.isOnFocus = in.readInt();
    }

    public static final Creator<EquipmentBean> CREATOR = new Creator<EquipmentBean>() {
        @Override
        public EquipmentBean createFromParcel(Parcel source) {
            return new EquipmentBean(source);
        }

        @Override
        public EquipmentBean[] newArray(int size) {
            return new EquipmentBean[size];
        }
    };
}
