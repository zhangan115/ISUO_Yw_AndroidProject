package com.isuo.yw2application.mode.bean.equip;

import com.isuo.yw2application.mode.bean.User;

/**
 * 关注
 * Created by zhangan on 2018/5/8.
 */

public class FocusBean {


    private long createTime;
    private long endTime;
    private int focusState;
    private long id;
    private User user;
    private String description;
    private EquipmentBean equipment;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public EquipmentBean getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentBean equipment) {
        this.equipment = equipment;
    }

    public int getFocusState() {
        return focusState;
    }

    public void setFocusState(int focusState) {
        this.focusState = focusState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
