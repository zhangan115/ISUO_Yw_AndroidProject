package com.isuo.yw2application.mode.inject.bean;

/**
 * 注油上传的数据
 * Created by zhangan on 2018/4/13.
 */

public class UploadBean {
    private long itemId;
    private String value;

    public UploadBean(long itemId, String value) {
        this.itemId = itemId;
        this.value = value;
    }

    public UploadBean() {
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
