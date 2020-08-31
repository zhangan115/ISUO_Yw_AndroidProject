package com.sito.customer.mode.bean.equip;

/**
 * Created by Yangzb on 2017/7/17 14:00
 * E-mail：yangzongbin@si-top.com
 * 配电室
 */
public class EquipRoom {

    /**
     * createTime : 1498096392000
     * deleteState : 0
     * roomId : 1
     * roomName : 1#配电室
     * roomRemark : 1号配电室
     */
    private boolean isSelect;
    private long createTime;
    private int deleteState;
    private int roomId;
    private String roomName;
    private String roomRemark;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomRemark() {
        return roomRemark;
    }

    public void setRoomRemark(String roomRemark) {
        this.roomRemark = roomRemark;
    }
}
