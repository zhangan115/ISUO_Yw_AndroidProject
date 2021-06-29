package com.isuo.yw2application.mode.bean.fault;

/**
 * Created by zhangan on 2017/8/23.
 */

public class AlarmCount {
    private int allCount;
    private int aFaultCount;
    private int bFaultCount;
    private int cFaultCount;

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getaFaultCount() {
        return aFaultCount;
    }

    public void setaFaultCount(int aFaultCount) {
        this.aFaultCount = aFaultCount;
    }

    public int getbFaultCount() {
        return bFaultCount;
    }

    public void setbFaultCount(int bFaultCount) {
        this.bFaultCount = bFaultCount;
    }

    public int getcFaultCount() {
        return cFaultCount;
    }

    public void setcFaultCount(int cFaultCount) {
        this.cFaultCount = cFaultCount;
    }
}
