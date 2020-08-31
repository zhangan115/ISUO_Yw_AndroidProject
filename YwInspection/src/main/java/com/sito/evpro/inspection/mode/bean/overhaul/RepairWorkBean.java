package com.sito.evpro.inspection.mode.bean.overhaul;

import com.sito.evpro.inspection.mode.bean.User;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;

import java.util.List;

/**
 * Created by zhangan on 2017-07-07.
 */

public class RepairWorkBean {

    private long commitTime;
    private long createTime;
    private long endTime;
    private EquipmentBean equipment;
    private FaultBean fault;
    private long repairId;
    private String repairName;
    private int repairResult;
    private int repairState;
    private long startTime;
    private UserBean user;
    private String voiceUrl;
    private String repairRemark;
    private int soundTimescale;
    private List<RepairPicsBean> repairPics;
    private List<RepairUsers> repairUsers;

    public List<RepairUsers> getRepairUsers() {
        return repairUsers;
    }

    public void setRepairUsers(List<RepairUsers> repairUsers) {
        this.repairUsers = repairUsers;
    }

    public String getRepairRemark() {
        return repairRemark;
    }

    public void setRepairRemark(String repairRemark) {
        this.repairRemark = repairRemark;
    }

    public int getSoundTimescale() {
        return soundTimescale;
    }

    public void setSoundTimescale(int soundTimescale) {
        this.soundTimescale = soundTimescale;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

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

    public FaultBean getFault() {
        return fault;
    }

    public void setFault(FaultBean fault) {
        this.fault = fault;
    }

    public long getRepairId() {
        return repairId;
    }

    public void setRepairId(long repairId) {
        this.repairId = repairId;
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public int getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(int repairResult) {
        this.repairResult = repairResult;
    }

    public int getRepairState() {
        return repairState;
    }

    public void setRepairState(int repairState) {
        this.repairState = repairState;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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

    public List<RepairPicsBean> getRepairPics() {
        return repairPics;
    }

    public void setRepairPics(List<RepairPicsBean> repairPics) {
        this.repairPics = repairPics;
    }


    public static class FaultBean {

        private long closeTime;
        private long createTime;
        private String faultDescript;
        private long faultId;
        private int faultState;
        private int faultType;
        private String voiceUrl;
        private User user;
        private int soundTimescale;
        private List<faultPicsBean> faultPics;

        public List<faultPicsBean> getFaultPics() {
            return faultPics;
        }

        public void setFaultPics(List<faultPicsBean> faultPics) {
            this.faultPics = faultPics;
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

        public String getVoiceUrl() {
            return voiceUrl;
        }

        public void setVoiceUrl(String voiceUrl) {
            this.voiceUrl = voiceUrl;
        }
    }

    public static class UserBean {


        private int deleteState;
        private long joinTime;
        private String realName;
        private long userId;
        private String userName;
        private String userPhone;
        private int userType;

        public int getDeleteState() {
            return deleteState;
        }

        public void setDeleteState(int deleteState) {
            this.deleteState = deleteState;
        }

        public long getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }


    public static class RepairPicsBean {

        private long id;
        private String picUrl;

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
    }


    public static class faultPicsBean {
        private long id;
        private String picUrl;

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
    }
}
