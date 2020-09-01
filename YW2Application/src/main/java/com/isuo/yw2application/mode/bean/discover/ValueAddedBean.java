package com.isuo.yw2application.mode.bean.discover;

import java.util.List;

/**
 * 增值工作
 * Created by zhangan on 2018/4/17.
 */

public class ValueAddedBean {

    private int firstResult;
    private List<Data> list;

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    public static class Data {

        private int dispalyState;
        private String iconUrl;
        private long publishingTime;
        private int readingCount;
        private String valueAddedContent;
        private long valueAddedId;
        private String valueAddedTitle;
        private String valueUrl;

        public int getDispalyState() {
            return dispalyState;
        }

        public void setDispalyState(int dispalyState) {
            this.dispalyState = dispalyState;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public long getPublishingTime() {
            return publishingTime;
        }

        public void setPublishingTime(long publishingTime) {
            this.publishingTime = publishingTime;
        }

        public int getReadingCount() {
            return readingCount;
        }

        public void setReadingCount(int readingCount) {
            this.readingCount = readingCount;
        }

        public String getValueAddedContent() {
            return valueAddedContent;
        }

        public void setValueAddedContent(String valueAddedContent) {
            this.valueAddedContent = valueAddedContent;
        }

        public long getValueAddedId() {
            return valueAddedId;
        }

        public void setValueAddedId(long valueAddedId) {
            this.valueAddedId = valueAddedId;
        }

        public String getValueAddedTitle() {
            return valueAddedTitle;
        }

        public void setValueAddedTitle(String valueAddedTitle) {
            this.valueAddedTitle = valueAddedTitle;
        }

        public String getValueUrl() {
            return valueUrl;
        }

        public void setValueUrl(String valueUrl) {
            this.valueUrl = valueUrl;
        }
    }
}
