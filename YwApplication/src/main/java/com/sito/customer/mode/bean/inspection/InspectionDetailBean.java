package com.sito.customer.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangan on 2017-07-07.
 */

public class InspectionDetailBean implements Parcelable {

    private long endTime;
    private int isManualCreated;
    private long planEndTime;
    private long planStartTime;
    private long startTime;
    private long taskId;
    private String taskName;
    private int taskState;
    private List<ExecutorUserList> executorUserList;
    private List<RoomListBean> roomList;

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getIsManualCreated() {
        return isManualCreated;
    }

    public void setIsManualCreated(int isManualCreated) {
        this.isManualCreated = isManualCreated;
    }

    public long getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(long planEndTime) {
        this.planEndTime = planEndTime;
    }

    public long getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(long planStartTime) {
        this.planStartTime = planStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public List<ExecutorUserList> getExecutorUserList() {
        return executorUserList;
    }

    public void setExecutorUserList(List<ExecutorUserList> executorUserList) {
        this.executorUserList = executorUserList;
    }

    public List<RoomListBean> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomListBean> roomList) {
        this.roomList = roomList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.endTime);
        dest.writeInt(this.isManualCreated);
        dest.writeLong(this.planEndTime);
        dest.writeLong(this.planStartTime);
        dest.writeLong(this.startTime);
        dest.writeLong(this.taskId);
        dest.writeString(this.taskName);
        dest.writeInt(this.taskState);
        dest.writeTypedList(this.executorUserList);
        dest.writeTypedList(this.roomList);
    }

    public InspectionDetailBean() {
    }

    protected InspectionDetailBean(Parcel in) {
        this.endTime = in.readLong();
        this.isManualCreated = in.readInt();
        this.planEndTime = in.readLong();
        this.planStartTime = in.readLong();
        this.startTime = in.readLong();
        this.taskId = in.readLong();
        this.taskName = in.readString();
        this.taskState = in.readInt();
        this.executorUserList = in.createTypedArrayList(ExecutorUserList.CREATOR);
        this.roomList = in.createTypedArrayList(RoomListBean.CREATOR);
    }

    public static final Creator<InspectionDetailBean> CREATOR = new Creator<InspectionDetailBean>() {
        @Override
        public InspectionDetailBean createFromParcel(Parcel source) {
            return new InspectionDetailBean(source);
        }

        @Override
        public InspectionDetailBean[] newArray(int size) {
            return new InspectionDetailBean[size];
        }
    };
}
