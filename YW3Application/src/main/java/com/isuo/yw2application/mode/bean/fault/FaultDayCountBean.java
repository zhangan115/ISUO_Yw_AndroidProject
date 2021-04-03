package com.isuo.yw2application.mode.bean.fault;

/**
 * Created by zhangan on 2017-07-17.
 */

public class FaultDayCountBean {

    private int flowingCount;
    private int pendingCount;
    private int closeCount;
    private int allFault;
    private int repairCount;

    public int getFlowingCount() {
        return flowingCount;
    }

    public void setFlowingCount(int flowingCount) {
        this.flowingCount = flowingCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(int closeCount) {
        this.closeCount = closeCount;
    }

    public int getAllFault() {
        return allFault;
    }

    public void setAllFault(int allFault) {
        this.allFault = allFault;
    }

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }
}
