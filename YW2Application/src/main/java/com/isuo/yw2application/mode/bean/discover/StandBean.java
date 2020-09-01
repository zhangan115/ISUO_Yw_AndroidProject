package com.isuo.yw2application.mode.bean.discover;

import java.util.List;

/**
 * Created by Yangzb on 2017/8/24 14:07
 * E-mail：yangzongbin@si-top.com
 * 规章制度
 */
public class StandBean {

    /**
     * firstResult : 0
     * list : [{"createDeptName":"fwea","createPersonName":"gew","createTime":1503457185000,"customerId":1,"deleteState":0,"iconUrl":"http://localhost:8888/sitopeuv/file/1/regulation/image/1503458856441n2_100797.jpg@596w_1l.jpg","implStartTime":1504176600000,"regulationContent":"<p>awegewa<\/p>","regulationId":39,"regulationName":"fweaf"}]
     * pageNumber : 1
     * totalCount : 4
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
         * createDeptName : fwea
         * createPersonName : gew
         * createTime : 1503457185000
         * customerId : 1
         * deleteState : 0
         * iconUrl : http://localhost:8888/sitopeuv/file/1/regulation/image/1503458856441n2_100797.jpg@596w_1l.jpg
         * implStartTime : 1504176600000
         * regulationContent : <p>awegewa</p>
         * regulationId : 39
         * regulationName : fweaf
         */

        private String createDeptName;
        private String createPersonName;
        private long createTime;
        private int customerId;
        private int deleteState;
        private String iconUrl;
        private String regulationContent;
        private int regulationId;
        private String regulationName;

        public String getCreateDeptName() {
            return createDeptName;
        }

        public void setCreateDeptName(String createDeptName) {
            this.createDeptName = createDeptName;
        }

        public String getCreatePersonName() {
            return createPersonName;
        }

        public void setCreatePersonName(String createPersonName) {
            this.createPersonName = createPersonName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getDeleteState() {
            return deleteState;
        }

        public void setDeleteState(int deleteState) {
            this.deleteState = deleteState;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getRegulationContent() {
            return regulationContent;
        }

        public void setRegulationContent(String regulationContent) {
            this.regulationContent = regulationContent;
        }

        public int getRegulationId() {
            return regulationId;
        }

        public void setRegulationId(int regulationId) {
            this.regulationId = regulationId;
        }

        public String getRegulationName() {
            return regulationName;
        }

        public void setRegulationName(String regulationName) {
            this.regulationName = regulationName;
        }
    }
}
