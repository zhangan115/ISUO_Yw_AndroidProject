package com.sito.customer.mode.bean.fault;

import java.util.List;

/**
 * Created by zhangan on 2017/9/8.
 */

public class JobPackageBean {


    /**
     * firstResult : 0
     * list : [{"createTime":1504752998000,"deleteState":0,"jobId":8,"jobName":"111","jobRemark":"222"}]
     * pageNumber : 1
     * totalCount : 1
     */

    private int firstResult;
    private int pageNumber;
    private int totalCount;
    private List<ListBean> list;

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * createTime : 1504752998000
         * deleteState : 0
         * jobId : 8
         * jobName : 111
         * jobRemark : 222
         */

        private long createTime;
        private int deleteState;
        private int jobId;
        private String jobName;
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

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getJobRemark() {
            return jobRemark;
        }

        public void setJobRemark(String jobRemark) {
            this.jobRemark = jobRemark;
        }
    }
}
