package com.isuo.yw2application.mode.bean.news;

/**
 * Created by Yangzb on 2017/7/14 14:02
 * E-mail：yangzongbin@si-top.com
 * 今日工作计划
 */
public class Plan {

    /**
     * taskCount : 2
     * incrementCount : 0
     * repairCount : 5
     */

    private int taskCount;
    private int incrementCount;
    private int repairCount;

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
}
