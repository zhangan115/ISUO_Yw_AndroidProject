package com.isuo.yw2application.mode.fire;


import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Yangzb on 2017/7/7 11:41
 * E-mail：yangzongbin@si-top.com
 * 设备列表 带配电室
 */
public class FireListBean implements Comparable<FireListBean> {

    private int count;
    private int roomId;
    private String roomName;
    private List<FireBean> fireEquipmentList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<FireBean> getEquipments() {
        return fireEquipmentList;
    }

    public void setEquipments(List<FireBean> equipments) {
        this.fireEquipmentList = equipments;
    }


    @Override
    public int compareTo(@NonNull FireListBean fireListBean) {
        return this.getCount() - fireListBean.getCount();
    }
}
