package com.isuo.yw2application.mode.bean.count;

/**
 * Created by zhangan on 2017-07-19.
 */

public class WorkCount {

    private String realName;
    private int taskCount;
    private int incrementCount;
    private int repairCount;
    private long userId;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getIncrementCount() {
        return incrementCount;
    }

    public void setIncrementCount(int incrementCount) {
        this.incrementCount = incrementCount;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
