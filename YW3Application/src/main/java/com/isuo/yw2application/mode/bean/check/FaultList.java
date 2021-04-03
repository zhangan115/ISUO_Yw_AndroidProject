package com.isuo.yw2application.mode.bean.check;

import com.isuo.yw2application.mode.bean.User;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.isuo.yw2application.mode.bean.overhaul.RepairUsers;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/6 15:37
 * E-mail：yangzongbin@si-top.com
 */
public class FaultList {


    private boolean isPlay = false;
    private long closeTime;
    private long createTime;
    private EquipmentBean equipment;
    private String faultDescript;
    private int faultId;
    private int faultState;
    private int faultType;
    private int soundTimescale;
    private User user;
    private String voiceUrl;
    private List<FaultPicsBean> faultPics;
    private List<RepairUsers> repairUsers;

    public List<RepairUsers> getRepairUsers() {
        return repairUsers;
    }

    public void setRepairUsers(List<RepairUsers> repairUsers) {
        this.repairUsers = repairUsers;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public EquipmentBean getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentBean equipment) {
        this.equipment = equipment;
    }

    public String getFaultDescript() {
        return faultDescript;
    }

    public void setFaultDescript(String faultDescript) {
        this.faultDescript = faultDescript;
    }

    public int getFaultId() {
        return faultId;
    }

    public void setFaultId(int faultId) {
        this.faultId = faultId;
    }

    public int getFaultState() {
        return faultState;
    }

    public void setFaultState(int faultState) {
        this.faultState = faultState;
    }

    public int getFaultType() {
        return faultType;
    }

    public void setFaultType(int faultType) {
        this.faultType = faultType;
    }

    public int getSoundTimescale() {
        return soundTimescale;
    }

    public void setSoundTimescale(int soundTimescale) {
        this.soundTimescale = soundTimescale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public List<FaultPicsBean> getFaultPics() {
        return faultPics;
    }

    public void setFaultPics(List<FaultPicsBean> faultPics) {
        this.faultPics = faultPics;
    }



    public static class FaultPicsBean {

        private int id;
        private String picUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
