package com.sito.customer.mode.bean.discover;

/**
 * Created by Yangzb on 2017/7/18 10:25
 * E-mail：yangzongbin@si-top.com
 */
public class DeptType {

    /**
     * createTime : 1499922821000
     * deleteState : 0
     * deptId : 1
     * deptLevel : 1
     * deptName : 运行保障部
     * deptRemark : 水电费
     * deptType : 1
     * parentId : -1
     */

    private long createTime;
    private int deleteState;
    private int deptId;
    private int deptLevel;
    private String deptName;
    private String deptRemark;
    private int deptType;
    private int parentId;

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

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(int deptLevel) {
        this.deptLevel = deptLevel;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptRemark() {
        return deptRemark;
    }

    public void setDeptRemark(String deptRemark) {
        this.deptRemark = deptRemark;
    }

    public int getDeptType() {
        return deptType;
    }

    public void setDeptType(int deptType) {
        this.deptType = deptType;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
