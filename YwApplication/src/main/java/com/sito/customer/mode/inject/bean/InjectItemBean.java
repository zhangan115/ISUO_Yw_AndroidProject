package com.sito.customer.mode.inject.bean;

import java.util.List;

/**
 * 注油item
 * Created by zhangan on 2018/4/13.
 */

public class InjectItemBean {

    private int firstResult;
    private List<InjectOilBean> list;
    private int pageNumber;
    private int totalCount;

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public List<InjectOilBean> getList() {
        return list;
    }

    public void setList(List<InjectOilBean> list) {
        this.list = list;
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
}
