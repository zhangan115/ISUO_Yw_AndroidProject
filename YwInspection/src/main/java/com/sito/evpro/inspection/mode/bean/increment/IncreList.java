package com.sito.evpro.inspection.mode.bean.increment;

import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;

/**
 * Created by Yangzb on 2017/7/6 18:00
 * E-mail：yangzongbin@si-top.com
 * 增值工作
 */
public class IncreList {

    private long createTime;
    private EquipmentBean equipment;
    private int operation;
    private int soundTimescale;
    private int sourceId;
    private String sourceName;
    private String workContent;
    private int workId;
    private String workImages;
    private String workSound;
    private int workType;
    private String workTypeName;
    private boolean isPlay = false;

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
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

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getSoundTimescale() {
        return soundTimescale;
    }

    public void setSoundTimescale(int soundTimescale) {
        this.soundTimescale = soundTimescale;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public String getWorkImages() {
        return workImages;
    }

    public void setWorkImages(String workImages) {
        this.workImages = workImages;
    }

    public String getWorkSound() {
        return workSound;
    }

    public void setWorkSound(String workSound) {
        this.workSound = workSound;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }
}
