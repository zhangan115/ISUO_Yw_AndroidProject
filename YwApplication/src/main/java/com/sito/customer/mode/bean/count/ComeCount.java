package com.sito.customer.mode.bean.count;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/14 16:30
 * E-mail：yangzongbin@si-top.com
 * 人员到岗统计
 */
public class ComeCount {

    /**
     * isManualCreated : 1
     * planEndTime : 1500015000000
     * planStartTime : 1499117100000
     * taskId : 46
     * taskName : 123
     * taskState : 2
     * taskStationState : 1
     */

    private int isManualCreated;
    private long planEndTime;
    private long planStartTime;
    private long startTime;
    private long endTime;
    private int taskId;
    private String taskName;
    private int taskState;// 1未到岗 2 到岗 3未开始 4延期
    private int taskStationState;
    private List<String> rooms;

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

    public int getTaskStationState() {
        return taskStationState;
    }

    public void setTaskStationState(int taskStationState) {
        this.taskStationState = taskStationState;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }
}
