package com.isuo.yw2application.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2017-07-10.
 */

public class RoomBean  implements Parcelable{
    private long createTime;
    private int deleteState;
    private int roomId;
    private String roomName;

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

    private String roomRemark;

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
    }

    public RoomBean() {
    }

    protected RoomBean(Parcel in) {
        this.createTime = in.readLong();
        this.deleteState = in.readInt();
        this.roomId = in.readInt();
        this.roomName = in.readString();
        this.roomRemark = in.readString();
    }

    public static final Creator<RoomBean> CREATOR = new Creator<RoomBean>() {
        @Override
        public RoomBean createFromParcel(Parcel source) {
            return new RoomBean(source);
        }

        @Override
        public RoomBean[] newArray(int size) {
            return new RoomBean[size];
        }
    };
}
