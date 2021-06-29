package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;


import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Yangzb on 2017/7/6 09:23
 * E-mail：yangzongbin@si-top.com
 * 拍照
 */
@Entity(nameInDb = "file_image")
public class Image implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;
    private Long saveTime;
    private String imageLocal;
    private String backUrl;
    private boolean isUpload;
    private int workType;//业务类型
    private long itemId;
    private long currentUserId = Yw2Application.getInstance().getCurrentUser().getUserId();//使用者id


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.saveTime);
        dest.writeString(this.imageLocal);
        dest.writeString(this.backUrl);
        dest.writeByte(this.isUpload ? (byte) 1 : (byte) 0);
        dest.writeInt(this.workType);
        dest.writeLong(this.itemId);
        dest.writeLong(this.currentUserId);
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.saveTime = (Long) in.readValue(Long.class.getClassLoader());
        this.imageLocal = in.readString();
        this.backUrl = in.readString();
        this.isUpload = in.readByte() != 0;
        this.workType = in.readInt();
        this.itemId = in.readLong();
        this.currentUserId = in.readLong();
    }

    @Generated(hash = 1508810006)
    public Image(Long _id, Long saveTime, String imageLocal, String backUrl, boolean isUpload,
            int workType, long itemId, long currentUserId) {
        this._id = _id;
        this.saveTime = saveTime;
        this.imageLocal = imageLocal;
        this.backUrl = backUrl;
        this.isUpload = isUpload;
        this.workType = workType;
        this.itemId = itemId;
        this.currentUserId = currentUserId;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
