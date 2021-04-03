package com.isuo.yw2application.mode.inject.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.isuo.yw2application.widget.treeadapter.RvTree;


/**
 * 注油区域
 * Created by zhangan on 2017/8/29.
 */

public class InjectRoomBean implements Parcelable, RvTree {

    private long createTime;
    private long roomId;
    private long parentRoomId;
    private String roomName;
    private String roomRemark;
    private int roomType;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getParentRoomId() {
        return parentRoomId;
    }

    public void setParentRoomId(long parentRoomId) {
        this.parentRoomId = parentRoomId;
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

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeLong(this.roomId);
        dest.writeLong(this.parentRoomId);
        dest.writeString(this.roomName);
        dest.writeString(this.roomRemark);
        dest.writeInt(this.roomType);
    }

    public InjectRoomBean() {
    }

    protected InjectRoomBean(Parcel in) {
        this.createTime = in.readLong();
        this.roomId = in.readLong();
        this.parentRoomId = in.readLong();
        this.roomName = in.readString();
        this.roomRemark = in.readString();
        this.roomType = in.readInt();
    }

    public static final Creator<InjectRoomBean> CREATOR = new Creator<InjectRoomBean>() {
        @Override
        public InjectRoomBean createFromParcel(Parcel source) {
            return new InjectRoomBean(source);
        }

        @Override
        public InjectRoomBean[] newArray(int size) {
            return new InjectRoomBean[size];
        }
    };

    @Override
    public long getNid() {
        return roomId;
    }

    @Override
    public long getPid() {
        return parentRoomId;
    }

    @Override
    public String getTitle() {
        return roomName;
    }

    @Override
    public int getImageResId() {
        return 0;
    }
}
