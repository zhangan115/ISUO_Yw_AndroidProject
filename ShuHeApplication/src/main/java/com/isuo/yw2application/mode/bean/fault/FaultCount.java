package com.isuo.yw2application.mode.bean.fault;

public class FaultCount {
//    {"data":{"flowingCount":7,"dayCount":1,"pendingCount":28,"closeCount":19,
//            "repairCount":25,"relicFaultCount":13},"errorCode":0}
    private int flowingCount;
    private int dayCount;
    private int pendingCount;
    private int closeCount;
    private int repairCount;
    private int relicFaultCount;

    public int getFlowingCount() {
        return flowingCount;
    }

    public void setFlowingCount(int flowingCount) {
        this.flowingCount = flowingCount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
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

    public int getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public int getRelicFaultCount() {
        return relicFaultCount;
    }

    public void setRelicFaultCount(int relicFaultCount) {
        this.relicFaultCount = relicFaultCount;
    }
}
