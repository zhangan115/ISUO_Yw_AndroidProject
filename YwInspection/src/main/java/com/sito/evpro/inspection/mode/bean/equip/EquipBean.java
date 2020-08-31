package com.sito.evpro.inspection.mode.bean.equip;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/7 11:41
 * E-mail：yangzongbin@si-top.com
 * 设备列表 带配电室
 */
public class EquipBean implements Parcelable {


    private long createTime;
    private int deleteState;
    private int roomId;
    private String roomName;
    private String roomRemark;
    private List<EquipmentBean> equipments;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomRemark() {
        return roomRemark;
    }

    public void setRoomRemark(String roomRemark) {
        this.roomRemark = roomRemark;
    }

    public List<EquipmentBean> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentBean> equipments) {
        this.equipments = equipments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.deleteState);
        dest.writeInt(this.roomId);
        dest.writeString(this.roomName);
        dest.writeString(this.roomRemark);
        dest.writeTypedList(this.equipments);
    }

    public EquipBean() {
    }

    protected EquipBean(Parcel in) {
        this.createTime = in.readLong();
        this.deleteState = in.readInt();
        this.roomId = in.readInt();
        this.roomName = in.readString();
        this.roomRemark = in.readString();
        this.equipments = in.createTypedArrayList(EquipmentBean.CREATOR);
    }

    public static final Creator<EquipBean> CREATOR = new Creator<EquipBean>() {
        @Override
        public EquipBean createFromParcel(Parcel source) {
            return new EquipBean(source);
        }

        @Override
        public EquipBean[] newArray(int size) {
            return new EquipBean[size];
        }
    };
}
