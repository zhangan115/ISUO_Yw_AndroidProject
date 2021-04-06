package com.isuo.yw2application.mode.fire;


import java.util.List;

/**
 * Created by Yangzb on 2017/7/7 11:41
 * E-mail：yangzongbin@si-top.com
 * 设备列表 带配电室
 */
public class FireListBean {

    private long createTime;
    private int deleteState;
    private int roomId;
    private String roomName;
    private String roomRemark;
    private List<FireBean> equipments;

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

    public List<FireBean> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<FireBean> equipments) {
        this.equipments = equipments;
    }
}
