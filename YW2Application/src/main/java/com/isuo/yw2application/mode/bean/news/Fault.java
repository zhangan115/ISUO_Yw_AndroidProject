package com.isuo.yw2application.mode.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yangzb on 2017/7/14 14:06
 * E-mail：yangzongbin@si-top.com
 * 今日对象故障
 */
public class Fault implements Parcelable {

    public Fault() {

    }

    public Fault(int flowingCount, int pendingCount, int closeCount, int allFault, int repairCount) {
        this.flowingCount = flowingCount;
        this.pendingCount = pendingCount;
        this.closeCount = closeCount;
        this.allFault = allFault;
        this.repairCount = repairCount;
    }

    /**
     * flowingCount : 0
     * pendingCount : 0
     * closeCount : 0
     * allFault : 0
     * repairCount : 0
     */

    private int flowingCount;//处理中
    private int pendingCount;
    private int closeCount;
    private int allFault;//上报
    private int repairCount;

    protected Fault(Parcel in) {
        flowingCount = in.readInt();
        pendingCount = in.readInt();
        closeCount = in.readInt();
        allFault = in.readInt();
        repairCount = in.readInt();
    }

    public static final Creator<Fault> CREATOR = new Creator<Fault>() {
        @Override
        public Fault createFromParcel(Parcel in) {
            return new Fault(in);
        }

        @Override
        public Fault[] newArray(int size) {
            return new Fault[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(flowingCount);
        dest.writeInt(pendingCount);
        dest.writeInt(closeCount);
        dest.writeInt(allFault);
        dest.writeInt(repairCount);
    }
}
