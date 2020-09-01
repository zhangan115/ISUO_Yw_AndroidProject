package com.isuo.yw2application.mode.bean.count;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/19 15:02
 * E-mail：yangzongbin@si-top.com
 */
public class WeekList {

    private List<WeekBean> week;

    public List<WeekBean> getWeek() {
        return week;
    }

    public void setWeek(List<WeekBean> week) {
        this.week = week;
    }

    public static class WeekBean {
        /**
         * isManualCreated : 0
         * planEndTime : 1500372000000
         * planStartTime : 1500339600000
         * taskId : 146
         * taskName : 西拓研发部办公室日点检任务(2017-07-18 09:00:00)
         * taskState : 1
         */

        private int isManualCreated;
        private long planEndTime;
        private long planStartTime;
        private int taskId;
        private String taskName;
        private int taskState;
        private long startTime;
        private long endTime;
        private List<String> rooms;

        public List<String> getRooms() {
            return rooms;
        }

        public void setRooms(List<String> rooms) {
            this.rooms = rooms;
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
    }
}
