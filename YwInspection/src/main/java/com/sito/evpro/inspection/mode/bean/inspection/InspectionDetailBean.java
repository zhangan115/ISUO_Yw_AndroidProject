package com.sito.evpro.inspection.mode.bean.inspection;

import java.util.List;

/**
 * Created by zhangan on 2017-07-07.
 */

public class InspectionDetailBean {

    private long endTime;
    private int isManualCreated;
    private long planEndTime;
    private long planStartTime;
    private long startTime;
    private int taskId;
    private String taskName;
    private int taskState;
    private List<ExecutorUserList> executorUserList;
    private List<RoomListBean> roomList;

    public List<ExecutorUserList> getExecutorUserList() {
        return executorUserList;
    }

    public void setExecutorUserList(List<ExecutorUserList> executorUserList) {
        this.executorUserList = executorUserList;
    }

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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
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

    public List<RoomListBean> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomListBean> roomList) {
        this.roomList = roomList;
    }
}
