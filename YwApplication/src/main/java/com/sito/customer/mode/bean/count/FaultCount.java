package com.sito.customer.mode.bean.count;

/**
 * Created by zhangan on 2017-07-19.
 */

public class FaultCount {

    private String realName;
    private int aFaultCount;
    private int cFaultCount;
    private long userId;
    private int bFaultCount;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAFaultCount() {
        return aFaultCount;
    }

    public void setAFaultCount(int aFaultCount) {
        this.aFaultCount = aFaultCount;
    }

    public int getCFaultCount() {
        return cFaultCount;
    }

    public void setCFaultCount(int cFaultCount) {
        this.cFaultCount = cFaultCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBFaultCount() {
        return bFaultCount;
    }

    public void setBFaultCount(int bFaultCount) {
        this.bFaultCount = bFaultCount;
    }
}
