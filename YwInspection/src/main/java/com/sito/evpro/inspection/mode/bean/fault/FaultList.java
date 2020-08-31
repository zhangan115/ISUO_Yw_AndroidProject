package com.sito.evpro.inspection.mode.bean.fault;

import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.evpro.inspection.mode.bean.overhaul.UserBean;

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
    private UserBean user;
    private String voiceUrl;
    private List<FaultPicsBean> faultPics;

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

    public List<FaultPicsBean> getFaultPics() {
        return faultPics;
    }

    public void setFaultPics(List<FaultPicsBean> faultPics) {
        this.faultPics = faultPics;
    }


    public static class FaultPicsBean {
        /**
         * id : 2
         * picUrl : http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=图片金毛&step_word=&hs=0&pn=2&spn=0&di=26067708260&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=1789419305%2C999974877&os=2405963703%2C3840219088&simid=3355391818%2C202510705&adpicid=0&lpn=0&ln=1974&fr=&fmq=1499153571813_R&fm=result&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fwww.ichong123.com%2Fuploads%2F2014%2F09%2F43675892.snap_1.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Btvi5g28dn_z%26e3Bv54AzdH3F4jg2vi5g2AzdH3Fl9n&gsm=0&rpstart=0&rpnum=0
         */

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
