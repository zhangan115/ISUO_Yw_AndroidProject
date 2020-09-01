package com.isuo.yw2application.mode.bean.news;

/**
 * 企业消息详情
 * Created by zhangan on 2018/4/23.
 */

public class EnterpriseDetailBean {
    private String appendicesUrl;
    private String content;
    private long createTime;
    private String title;

    public String getAppendicesUrl() {
        return appendicesUrl;
    }

    public void setAppendicesUrl(String appendicesUrl) {
        this.appendicesUrl = appendicesUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
