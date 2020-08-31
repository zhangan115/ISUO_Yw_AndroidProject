package com.sito.customer.mode.bean.db;


import com.sito.customer.app.CustomerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
    private long itemId;
    private long currentUserId = CustomerApp.getInstance().getCurrentUser().getUserId();//使用者id
    private String voiceTime;

    @Generated(hash = 421240853)
    public Voice(Long _id, Long saveTime, String voiceLocal, String backUrl, String mContent,
            boolean isUpload, int workType, long itemId, long currentUserId, String voiceTime) {
        this._id = _id;
        this.saveTime = saveTime;
        this.voiceLocal = voiceLocal;
        this.backUrl = backUrl;
        this.mContent = mContent;
        this.isUpload = isUpload;
        this.workType = workType;
        this.itemId = itemId;
        this.currentUserId = currentUserId;
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

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
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

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getMContent() {
        return this.mContent;
    }

    public void setMContent(String mContent) {
        this.mContent = mContent;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
