package com.sito.evpro.inspection.mode.bean.equip;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2017/10/13.
 */

public class TimeLineBean implements Parcelable {
    private long createTime;
    private int type;
    private long equipmentRecordId;
    private String recordContent;
    private String recordName;
    private String staffName;


    public TimeLineBean() {
    }


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int deleteState) {
        this.type = deleteState;
    }

    public long getEquipmentRecordId() {
        return equipmentRecordId;
    }

    public void setEquipmentRecordId(long equipmentRecordId) {
        this.equipmentRecordId = equipmentRecordId;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.type);
        dest.writeLong(this.equipmentRecordId);
        dest.writeString(this.recordContent);
        dest.writeString(this.recordName);
        dest.writeString(this.staffName);
    }

    protected TimeLineBean(Parcel in) {
        this.createTime = in.readLong();
        this.type = in.readInt();
        this.equipmentRecordId = in.readLong();
        this.recordContent = in.readString();
        this.recordName = in.readString();
        this.staffName = in.readString();
    }

    public static final Creator<TimeLineBean> CREATOR = new Creator<TimeLineBean>() {
        @Override
        public TimeLineBean createFromParcel(Parcel source) {
            return new TimeLineBean(source);
        }

        @Override
        public TimeLineBean[] newArray(int size) {
            return new TimeLineBean[size];
        }
    };
}
