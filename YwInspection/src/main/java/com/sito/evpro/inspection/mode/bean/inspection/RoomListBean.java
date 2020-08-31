package com.sito.evpro.inspection.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.evpro.inspection.mode.bean.db.RoomDb;

import java.util.List;

/**
 * Created by zhangan on 2017-07-10.
 */

public class RoomListBean implements Parcelable {

    private RoomBean room;
    private long startTime;
    private long taskRoomId;
    private int taskRoomState;
    private long endTime;
    private List<TaskEquipmentBean> taskEquipment;
    private int state;//本地属性 未开始 0， 进行中 1，已完成2
    private long lastSaveTime;//最后保存时间
    private RoomDb roomDb;

    public RoomDb getRoomDb() {
        return roomDb;
    }

    public void setRoomDb(RoomDb roomDb) {
        this.roomDb = roomDb;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTaskRoomId() {
        return taskRoomId;
    }

    public void setTaskRoomId(long taskRoomId) {
        this.taskRoomId = taskRoomId;
    }

    public int getTaskRoomState() {
        return taskRoomState;
    }

    public void setTaskRoomState(int taskRoomState) {
        this.taskRoomState = taskRoomState;
    }

    public List<TaskEquipmentBean> getTaskEquipment() {
        return taskEquipment;
    }

    public void setTaskEquipment(List<TaskEquipmentBean> taskEquipment) {
        this.taskEquipment = taskEquipment;
    }

    public RoomListBean() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.room, flags);
        dest.writeLong(this.startTime);
        dest.writeLong(this.taskRoomId);
        dest.writeInt(this.taskRoomState);
        dest.writeLong(this.endTime);
        dest.writeTypedList(this.taskEquipment);
        dest.writeInt(this.state);
        dest.writeLong(this.lastSaveTime);
        dest.writeParcelable(this.roomDb, flags);
    }

    protected RoomListBean(Parcel in) {
        this.room = in.readParcelable(RoomBean.class.getClassLoader());
        this.startTime = in.readLong();
        this.taskRoomId = in.readLong();
        this.taskRoomState = in.readInt();
        this.endTime = in.readLong();
        this.taskEquipment = in.createTypedArrayList(TaskEquipmentBean.CREATOR);
        this.state = in.readInt();
        this.lastSaveTime = in.readLong();
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
