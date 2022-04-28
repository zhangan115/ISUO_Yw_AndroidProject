package com.isuo.yw2application.mode.bean.equip;


import java.util.List;

/**
 * Created by Yangzb on 2017/7/7 11:41
 * E-mail：yangzongbin@si-top.com
 * 对象列表 带配电室
 */
public class EquipBean {

    private long createTime;
    private int deleteState;
    private int roomId;
    private String roomName;
    private String roomRemark;
    private List<EquipmentBean> equipments;

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

    public List<EquipmentBean> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentBean> equipments) {
        this.equipments = equipments;
    }
}
