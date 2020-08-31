package com.sito.evpro.inspection.mode.bean.db;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Yangzb on 2017/7/6 09:23
 * E-mail：yangzongbin@si-top.com
 * 拍照
 */
@Entity(nameInDb = "file_image")
public class Image {
    @Id(autoincrement = true)
    private Long _id;
    private Long saveTime;
    private String imageLocal;
    private String backUrl;
    private boolean isUpload;
    private int workType;//业务类型
    private int currentUserId = InspectionApp.getInstance().getCurrentUser().getUserId();//使用者id
    private long itemId;

    @Generated(hash = 1626271584)
    public Image(Long _id, Long saveTime, String imageLocal, String backUrl, boolean isUpload,
            int workType, int currentUserId, long itemId) {
        this._id = _id;
        this.saveTime = saveTime;
        this.imageLocal = imageLocal;
        this.backUrl = backUrl;
        this.isUpload = isUpload;
        this.workType = workType;
        this.currentUserId = currentUserId;
        this.itemId = itemId;
    }

    @Generated(hash = 1590301345)
    public Image() {
    }

    public Long get_id() {
        return _id;
    }


    public Long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }

    public String getImageLocal() {
        return imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
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


    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
