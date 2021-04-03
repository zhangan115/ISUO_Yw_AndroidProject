package com.isuo.yw2application.mode.bean;

import java.util.List;

public class EnterpriseCustomer {

    private int firstResult;
    private int pageNumber;
    private int totalCount;
    private List<User.CustomerBean> list;

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

    public List<User.CustomerBean> getList() {
        return list;
    }

    public void setList(List<User.CustomerBean> list) {
        this.list = list;
    }
}

