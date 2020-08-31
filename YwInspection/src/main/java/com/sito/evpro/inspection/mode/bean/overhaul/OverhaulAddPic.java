package com.sito.evpro.inspection.mode.bean.overhaul;

/**
 * Created by zhangan on 2017/10/12.
 */

public class OverhaulAddPic {

    private long id;
    private String picUrl;
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
