package com.sito.customer.mode.bean.overhaul;

import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.bean.work.UserBean;

import java.util.List;

/**
 * Created by zhangan on 2017-07-06.
 */

public class OverhaulBean {

    private long commitTime;
    private long createTime;
    private long endTime;
    private long repairId;
    private String repairName;
    private int repairResult;
    private int addType;
    private int repairState;
    private long startTime;
    private UserBean user;
    private EquipmentBean equipment;
    private FaultBean fault;
    private UserBean userExecute;
    private List<RepairUsers> repairUsers;
    private String voiceUrl;
    private int soundTimescaleAdd;
    private String voiceUrlAdd;
    private String repairIntro;
    private int isRemind;
    private List<OverhaulAddPic> repairPicsAdd;
    private List<OverhaulAddPic> repairPics;
    private int soundTimescale;
    private String repairRemark;
    private JobPackageBean jobPackage;


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

    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
    }

    public int getSoundTimescaleAdd() {
        return soundTimescaleAdd;
    }

    public void setSoundTimescaleAdd(int soundTimescaleAdd) {
        this.soundTimescaleAdd = soundTimescaleAdd;
    }

    public String getVoiceUrlAdd() {
        return voiceUrlAdd;
    }

    public void setVoiceUrlAdd(String voiceUrlAdd) {
        this.voiceUrlAdd = voiceUrlAdd;
    }

    public String getRepairIntro() {
        return repairIntro;
    }

    public void setRepairIntro(String repairIntro) {
        this.repairIntro = repairIntro;
    }

    public int getIsRemind() {
        return isRemind;
    }

    public List<OverhaulAddPic> getRepairPics() {
        return repairPics;
    }

    public void setRepairPics(List<OverhaulAddPic> repairPics) {
        this.repairPics = repairPics;
    }

    public void setIsRemind(int isRemind) {
        this.isRemind = isRemind;
    }

    public List<OverhaulAddPic> getRepairPicsAdd() {
        return repairPicsAdd;
    }

    public void setRepairPicsAdd(List<OverhaulAddPic> repairPicsAdd) {
        this.repairPicsAdd = repairPicsAdd;
    }

    public List<RepairUsers> getRepairUsers() {
        return repairUsers;
    }

    public void setRepairUsers(List<RepairUsers> repairUsers) {
        this.repairUsers = repairUsers;
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

    public UserBean getUserExecute() {
        return userExecute;
    }

    public void setUserExecute(UserBean userExecute) {
        this.userExecute = userExecute;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public JobPackageBean getJobPackage() {
        return jobPackage;
    }

    public void setJobPackage(JobPackageBean jobPackage) {
        this.jobPackage = jobPackage;
    }
}
