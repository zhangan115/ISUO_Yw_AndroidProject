package com.sito.customer.mode.bean.work;

/**
 * Created by zhangan on 2017/10/17.
 */

public class WorkCount {


    /**
     * taskCount : 2
     * taskFinishCount : 1
     * repairFinishCount : 1
     * fault : 7
     * repairCount : 2
     * workFinishCount : 0
     * workCount : 1
     */

    private int taskCount;
    private int taskFinishCount;
    private int repairFinishCount;
    private int fault;
    private int repairCount;
    private int workFinishCount;
    private int workCount;

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getTaskFinishCount() {
        return taskFinishCount;
    }

    public void setTaskFinishCount(int taskFinishCount) {
        this.taskFinishCount = taskFinishCount;
    }

    public int getRepairFinishCount() {
        return repairFinishCount;
    }

    public void setRepairFinishCount(int repairFinishCount) {
        this.repairFinishCount = repairFinishCount;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public int getWorkFinishCount() {
        return workFinishCount;
    }

    public void setWorkFinishCount(int workFinishCount) {
        this.workFinishCount = workFinishCount;
    }

    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }
}
