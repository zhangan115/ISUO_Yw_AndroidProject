package com.isuo.yw2application.mode.bean.overhaul;

/**
 * Created by zhangan on 2017/9/8.
 */

public class JobPackageBean {

    /**
     * createTime : 1504689073000
     * deleteState : 0
     * jobId : 9
     * jobName : 检修作业手册
     * jobRemark : 检修作业手册
     */

    private long createTime;
    private int deleteState;
    private long securityId;
    private String securityName;
    private String jobRemark;

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

    public long getJobId() {
        return securityId;
    }

    public void setJobId(long jobId) {
        this.securityId = jobId;
    }

    public String getJobName() {
        return securityName;
    }

    public void setJobName(String jobName) {
        this.securityName = jobName;
    }

    public String getJobRemark() {
        return jobRemark;
    }

    public void setJobRemark(String jobRemark) {
        this.jobRemark = jobRemark;
    }
}
