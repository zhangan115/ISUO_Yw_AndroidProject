package com.isuo.yw2application.mode.bean.inspection;

public class SecurityPackage {


    /**
     * createTime : 1500003846000
     * deleteState : 0
     * securityId : 9
     * securityName : 西拓研发部安全包
     * securityRemark : 西拓研发部安全包
     */

    private long createTime;
    private int deleteState;
    private int securityId;
    private String securityName;
    private String securityRemark;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getSecurityRemark() {
        return securityRemark;
    }

    public void setSecurityRemark(String securityRemark) {
        this.securityRemark = securityRemark;
    }
}
