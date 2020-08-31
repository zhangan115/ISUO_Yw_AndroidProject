package com.sito.customer.mode.bean.fault;

import java.util.List;

/**
 * Created by zhangan on 2017-07-17.
 */

public class FaultYearCountBean {

    private List<Integer> allList;//总数
    private List<Integer> pendingList;//待处理
    private List<Integer> flowingList;//处理中
    private List<Integer> repairList;//转检修
    private List<Integer> closeList;//关闭

    public List<Integer> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<Integer> repairList) {
        this.repairList = repairList;
    }

    public List<Integer> getAllList() {
        return allList;
    }

    public void setAllList(List<Integer> allList) {
        this.allList = allList;
    }

    public List<Integer> getFlowingList() {
        return flowingList;
    }

    public void setFlowingList(List<Integer> flowingList) {
        this.flowingList = flowingList;
    }

    public List<Integer> getCloseList() {
        return closeList;
    }

    public void setCloseList(List<Integer> closeList) {
        this.closeList = closeList;
    }

    public List<Integer> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<Integer> pendingList) {
        this.pendingList = pendingList;
    }
}
