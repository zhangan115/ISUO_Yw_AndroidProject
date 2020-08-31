package com.sito.customer.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;


import com.sito.customer.mode.bean.db.RoomDb;

import java.util.List;

/**
 * Created by zhangan on 2017-07-10.
 */

public class RoomListBean implements Parcelable {

    private int taskRoomState;
    private long taskRoomId;
    private long startTime;
    private long endTime;
    private List<TaskEquipmentBean> taskEquipment;
    private int state;//本地属性 未开始 0， 进行中 1，已完成2
    private long lastSaveTime;//最后保存时间
    private RoomBean room;
    private RoomDb roomDb;

    public int getTaskRoomState() {
        return taskRoomState;
    }

    public void setTaskRoomState(int taskRoomState) {
        this.taskRoomState = taskRoomState;
    }

    public long getTaskRoomId() {
        return taskRoomId;
    }

    public void setTaskRoomId(long taskRoomId) {
        this.taskRoomId = taskRoomId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<TaskEquipmentBean> getTaskEquipment() {
        return taskEquipment;
    }

    public void setTaskEquipment(List<TaskEquipmentBean> taskEquipment) {
        this.taskEquipment = taskEquipment;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public RoomDb getRoomDb() {
        return roomDb;
    }

    public void setRoomDb(RoomDb roomDb) {
        this.roomDb = roomDb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskRoomState);
        dest.writeLong(this.taskRoomId);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeTypedList(this.taskEquipment);
        dest.writeInt(this.state);
        dest.writeLong(this.lastSaveTime);
        dest.writeParcelable(this.room, flags);
        dest.writeParcelable(this.roomDb, flags);
    }

    public RoomListBean() {
    }

    protected RoomListBean(Parcel in) {
        this.taskRoomState = in.readInt();
        this.taskRoomId = in.readLong();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.taskEquipment = in.createTypedArrayList(TaskEquipmentBean.CREATOR);
        this.state = in.readInt();
        this.lastSaveTime = in.readLong();
        this.room = in.readParcelable(RoomBean.class.getClassLoader());
        this.roomDb = in.readParcelable(RoomDb.class.getClassLoader());
    }

    public static final Creator<RoomListBean> CREATOR = new Creator<RoomListBean>() {
        @Override
        public RoomListBean createFromParcel(Parcel source) {
            return new RoomListBean(source);
        }

        @Override
        public RoomListBean[] newArray(int size) {
            return new RoomListBean[size];
        }
    };
}
