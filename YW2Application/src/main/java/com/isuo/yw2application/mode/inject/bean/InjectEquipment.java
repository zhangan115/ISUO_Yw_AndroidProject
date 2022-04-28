package com.isuo.yw2application.mode.inject.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 注油对象
 * Created by zhangan on 2017/8/29.
 */

public class InjectEquipment implements Comparable<InjectEquipment>, Parcelable {

    private long createTime;
    private int cycle;
    private int deleteState;
    private long equipmentId;
    private String equipmentName;
    private String equipmentNumber;
    private String equipmentSn;
    private EquipmentInjectionOilBean injectionOil;
    private EquipmentInjectionOilBean beforeInjectionOil;
    private EquipmentInjectionOilBean backInjectionOil;
    private boolean isInject;
    private long time;

    @Override
    public int compareTo(@NonNull InjectEquipment o) {
        long i = o.getTime() - this.time;
        if (i == 0) {
            return 0;
        } else if (i > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
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

    public String getEquipmentSn() {
        return equipmentSn;
    }

    public void setEquipmentSn(String equipmentSn) {
        this.equipmentSn = equipmentSn;
    }

    public EquipmentInjectionOilBean getInjectionOil() {
        return injectionOil;
    }

    public void setInjectionOil(EquipmentInjectionOilBean injectionOil) {
        this.injectionOil = injectionOil;
    }

    public EquipmentInjectionOilBean getBeforeInjectionOil() {
        return beforeInjectionOil;
    }

    public void setBeforeInjectionOil(EquipmentInjectionOilBean beforeInjectionOil) {
        this.beforeInjectionOil = beforeInjectionOil;
    }

    public EquipmentInjectionOilBean getBackInjectionOil() {
        return backInjectionOil;
    }

    public void setBackInjectionOil(EquipmentInjectionOilBean backInjectionOil) {
        this.backInjectionOil = backInjectionOil;
    }

    public boolean isInject() {
        return isInject;
    }

    public void setInject(boolean inject) {
        isInject = inject;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.cycle);
        dest.writeInt(this.deleteState);
        dest.writeLong(this.equipmentId);
        dest.writeString(this.equipmentName);
        dest.writeString(this.equipmentNumber);
        dest.writeString(this.equipmentSn);
        dest.writeParcelable(this.injectionOil, flags);
        dest.writeParcelable(this.beforeInjectionOil, flags);
        dest.writeParcelable(this.backInjectionOil, flags);
        dest.writeByte(this.isInject ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time);
    }

    public InjectEquipment() {
    }

    protected InjectEquipment(Parcel in) {
        this.createTime = in.readLong();
        this.cycle = in.readInt();
        this.deleteState = in.readInt();
        this.equipmentId = in.readLong();
        this.equipmentName = in.readString();
        this.equipmentNumber = in.readString();
        this.equipmentSn = in.readString();
        this.injectionOil = in.readParcelable(EquipmentInjectionOilBean.class.getClassLoader());
        this.beforeInjectionOil = in.readParcelable(EquipmentInjectionOilBean.class.getClassLoader());
        this.backInjectionOil = in.readParcelable(EquipmentInjectionOilBean.class.getClassLoader());
        this.isInject = in.readByte() != 0;
        this.time = in.readLong();
    }

    public static final Creator<InjectEquipment> CREATOR = new Creator<InjectEquipment>() {
        @Override
        public InjectEquipment createFromParcel(Parcel source) {
            return new InjectEquipment(source);
        }

        @Override
        public InjectEquipment[] newArray(int size) {
            return new InjectEquipment[size];
        }
    };
}
