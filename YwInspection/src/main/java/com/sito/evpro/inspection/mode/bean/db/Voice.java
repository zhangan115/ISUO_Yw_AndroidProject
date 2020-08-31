package com.sito.evpro.inspection.mode.bean.db;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Yangzb on 2017/7/6 13:01
 * E-mail：yangzongbin@si-top.com
 * 录音
 */
@Entity(nameInDb = "file_voice")
public class Voice {
    @Id(autoincrement = true)
    private Long _id;
    private Long saveTime;
    private String voiceLocal;
    private String backUrl;
    private String mContent;
    private boolean isUpload;
    private int workType;//业务类型
    private int currentUserId = InspectionApp.getInstance().getCurrentUser().getUserId();//使用者id
    private long itemId;
    private String voiceTime;

    @Generated(hash = 443609993)
    public Voice(Long _id, Long saveTime, String voiceLocal, String backUrl, String mContent,
            boolean isUpload, int workType, int currentUserId, long itemId, String voiceTime) {
        this._id = _id;
        this.saveTime = saveTime;
        this.voiceLocal = voiceLocal;
        this.backUrl = backUrl;
        this.mContent = mContent;
        this.isUpload = isUpload;
        this.workType = workType;
        this.currentUserId = currentUserId;
        this.itemId = itemId;
        this.voiceTime = voiceTime;
    }

    @Generated(hash = 1158611544)
    public Voice() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }

    public String getVoiceLocal() {
        return voiceLocal;
    }

    public void setVoiceLocal(String voiceLocal) {
        this.voiceLocal = voiceLocal;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getMContent() {
        return this.mContent;
    }

    public void setMContent(String mContent) {
        this.mContent = mContent;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getVoiceTime() {
        return this.voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }
}
