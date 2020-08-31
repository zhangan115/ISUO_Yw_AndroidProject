package com.sito.customer.mode.bean.discover;

/**
 * Created by Yangzb on 2017/7/18 10:38
 * E-mail：yangzongbin@si-top.com
 */
public class FaultReport {

    /**
     * realName : 卞东锋
     * userId : 3
     * faultCount : 1
     */

    private String realName;
    private int userId;
    private int faultCount;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFaultCount() {
        return faultCount;
    }

    public void setFaultCount(int faultCount) {
        this.faultCount = faultCount;
    }
}
