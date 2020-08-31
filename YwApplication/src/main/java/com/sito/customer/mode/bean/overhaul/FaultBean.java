package com.sito.customer.mode.bean.overhaul;

import com.sito.customer.mode.bean.overhaul.OverhaulAddPic;
import com.sito.customer.mode.bean.work.UserBean;

import java.util.List;

/**
 * Created by zhangan on 2017/10/12.
 */

public class FaultBean {

    private long closeTime;
    private long createTime;
    private String faultDescript;
    private long faultId;
    private int faultState;
    private int faultType;
    private int soundTimescale;
    private UserBean user;
    private String voiceUrl;
    private List<OverhaulAddPic> faultPics;

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

    public String getFaultDescript() {
        return faultDescript;
    }

    public void setFaultDescript(String faultDescript) {
        this.faultDescript = faultDescript;
    }

    public long getFaultId() {
        return faultId;
    }

    public void setFaultId(long faultId) {
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public List<OverhaulAddPic> getFaultPics() {
        return faultPics;
    }

    public void setFaultPics(List<OverhaulAddPic> faultPics) {
        this.faultPics = faultPics;
    }
}
