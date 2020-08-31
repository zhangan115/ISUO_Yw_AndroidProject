package com.sito.evpro.inspection.mode.bean.inspection;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/23 16:43
 * E-mail：yangzongbin@si-top.com
 * 安全包
 */
public class SecureBean {

    /**
     * createTime : 1498116668000
     * deleteState : 0
     * deleteTime : 1498117453000
     * pageList : [{"pageContent":"巡检安全ba11111包","pageId":1,"pageName":"第一页 ","pageSequence":1},{"pageContent":"巡检安全ba大范甘迪发股份带个分   11111包","pageId":2,"pageName":"第三页 ","pageSequence":3},{"pageContent":"2","pageId":5,"pageName":"1","pageSequence":3},{"pageContent":"2","pageId":6,"pageName":"1","pageSequence":3},{"pageContent":"3","pageId":7,"pageName":"2","pageSequence":4},{"pageContent":"2","pageId":4,"pageName":"123","pageSequence":123}]
     * securityId : 3
     * securityName : 巡检安全ba11111包
     * securityRemark : sfdsfd打工的风格 风格和规范
     */

    private long createTime;
    private int deleteState;
    private long deleteTime;
    private int securityId;
    private String securityName;
    private String securityRemark;
    private List<PageListBean> pageList;

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

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
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

    public List<PageListBean> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageListBean> pageList) {
        this.pageList = pageList;
    }

    public static class PageListBean {
        /**
         * pageContent : 巡检安全ba11111包
         * pageId : 1
         * pageName : 第一页
         * pageSequence : 1
         */

        private String pageContent;
        private int pageId;
        private String pageName;
        private int pageSequence;

        public String getPageContent() {
            return pageContent;
        }

        public void setPageContent(String pageContent) {
            this.pageContent = pageContent;
        }

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public String getPageName() {
            return pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public int getPageSequence() {
            return pageSequence;
        }

        public void setPageSequence(int pageSequence) {
            this.pageSequence = pageSequence;
        }
    }
}
