package com.sito.customer.mode.bean.news;

import java.util.List;

/**
 * 消息列表
 * Created by zhangan on 2018/4/20.
 */

public class MessageListBean {

    private long createTime;
    private long messageId;
    private int messageType;
    private int tableId;
    private long taskId;
    private String title;
    private List<MessageItemListBean> messageItemList;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MessageItemListBean> getMessageItemList() {
        return messageItemList;
    }

    public void setMessageItemList(List<MessageItemListBean> messageItemList) {
        this.messageItemList = messageItemList;
    }

    public static class MessageItemListBean {

        private String content;
        private long createTime;
        private long itemId;
        private int messageType;
        private int smallType;
        private long tableId;
        private long taskId;
        private String contentDetail;

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

        public long getItemId() {
            return itemId;
        }

        public void setItemId(long itemId) {
            this.itemId = itemId;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public int getSmallType() {
            return smallType;
        }

        public void setSmallType(int smallType) {
            this.smallType = smallType;
        }

        public long getTableId() {
            return tableId;
        }

        public void setTableId(long tableId) {
            this.tableId = tableId;
        }

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        public String getContentDetail() {
            return contentDetail;
        }

        public void setContentDetail(String contentDetail) {
            this.contentDetail = contentDetail;
        }
    }
}
