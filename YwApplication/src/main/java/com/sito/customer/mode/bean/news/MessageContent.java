package com.sito.customer.mode.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.utils.ContentBean;

import java.io.Serializable;

/**
 * Created by zhangan on 2017/10/16.
 */

public class MessageContent implements Parcelable {

    private long messageTime;
    private int messageType;
    private String tip;
    private FromUserBean fromUser;
    private ContentInfoBean contentInfo;
    private String appContent;
    private long messageId;

    public String getAppContent() {
        return appContent;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent;
    }

    public ContentInfoBean getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(ContentInfoBean contentInfo) {
        this.contentInfo = contentInfo;
    }

    public FromUserBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUserBean fromUser) {
        this.fromUser = fromUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public static class ContentInfoBean implements Serializable{

        private int smallType;
        private String userIds;
        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public int getSmallType() {
            return smallType;
        }

        public void setSmallType(int smallType) {
            this.smallType = smallType;
        }

        public String getUserIds() {
            return userIds;
        }

        public void setUserIds(String userIds) {
            this.userIds = userIds;
        }
    }

    public static class FromUserBean implements Serializable {

        private int userId;
        private String realName;
        private String userName;
        private String userRoleNames;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserRoleNames() {
            return userRoleNames;
        }

        public void setUserRoleNames(String userRoleNames) {
            this.userRoleNames = userRoleNames;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.messageTime);
        dest.writeInt(this.messageType);
        dest.writeString(this.tip);
        dest.writeSerializable(this.fromUser);
        dest.writeSerializable(this.contentInfo);
        dest.writeString(this.appContent);
        dest.writeLong(this.messageId);
    }

    public MessageContent() {
    }

    protected MessageContent(Parcel in) {
        this.messageTime = in.readLong();
        this.messageType = in.readInt();
        this.tip = in.readString();
        this.fromUser = (FromUserBean) in.readSerializable();
        this.contentInfo = (ContentInfoBean) in.readSerializable();
        this.appContent = in.readString();
        this.messageId = in.readLong();
    }

    public static final Parcelable.Creator<MessageContent> CREATOR = new Parcelable.Creator<MessageContent>() {
        @Override
        public MessageContent createFromParcel(Parcel source) {
            return new MessageContent(source);
        }

        @Override
        public MessageContent[] newArray(int size) {
            return new MessageContent[size];
        }
    };
}
