package com.isuo.yw2application.mode.bean.db;

import android.os.Parcel;
import android.os.Parcelable;


import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangan on 2018/4/23.
 */
@Entity(nameInDb = "read_news")
public class ReadNewsBean implements Parcelable {

    private long messageId;
    private boolean isRead;
    private long createTime;
    private int messageType;
    private int userId = Yw2Application.getInstance().getCurrentUser().getUserId();

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public ReadNewsBean() {
    }

    @Generated(hash = 1712146754)
    public ReadNewsBean(long messageId, boolean isRead, long createTime, int messageType,
            int userId) {
        this.messageId = messageId;
        this.isRead = isRead;
        this.createTime = createTime;
        this.messageType = messageType;
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.messageId);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createTime);
        dest.writeInt(this.messageType);
        dest.writeInt(this.userId);
    }

    protected ReadNewsBean(Parcel in) {
        this.messageId = in.readLong();
        this.isRead = in.readByte() != 0;
        this.createTime = in.readLong();
        this.messageType = in.readInt();
        this.userId = in.readInt();
    }

    public static final Creator<ReadNewsBean> CREATOR = new Creator<ReadNewsBean>() {
        @Override
        public ReadNewsBean createFromParcel(Parcel source) {
            return new ReadNewsBean(source);
        }

        @Override
        public ReadNewsBean[] newArray(int size) {
            return new ReadNewsBean[size];
        }
    };
}
