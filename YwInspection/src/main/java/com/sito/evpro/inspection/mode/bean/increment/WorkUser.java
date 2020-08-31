package com.sito.evpro.inspection.mode.bean.increment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2017/10/11.
 */

public class WorkUser implements Parcelable{



    private long createTime;
    private int deleteState;
    private long joinTime;
    private String portraitUrl;
    private String realName;
    private String userCid;
    private long userId;
    private String userName;
    private String userPhone;
    private String userRoleNames;
    private String userTelephone;
    private int userType;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

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

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserCid() {
        return userCid;
    }

    public void setUserCid(String userCid) {
        this.userCid = userCid;
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

    public String getUserRoleNames() {
        return userRoleNames;
    }

    public void setUserRoleNames(String userRoleNames) {
        this.userRoleNames = userRoleNames;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeInt(this.deleteState);
        dest.writeLong(this.joinTime);
        dest.writeString(this.portraitUrl);
        dest.writeString(this.realName);
        dest.writeString(this.userCid);
        dest.writeLong(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeString(this.userRoleNames);
        dest.writeString(this.userTelephone);
        dest.writeInt(this.userType);
    }

    public WorkUser() {
    }

    protected WorkUser(Parcel in) {
        this.createTime = in.readLong();
        this.deleteState = in.readInt();
        this.joinTime = in.readLong();
        this.portraitUrl = in.readString();
        this.realName = in.readString();
        this.userCid = in.readString();
        this.userId = in.readLong();
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.userRoleNames = in.readString();
        this.userTelephone = in.readString();
        this.userType = in.readInt();
    }

    public static final Creator<WorkUser> CREATOR = new Creator<WorkUser>() {
        @Override
        public WorkUser createFromParcel(Parcel source) {
            return new WorkUser(source);
        }

        @Override
        public WorkUser[] newArray(int size) {
            return new WorkUser[size];
        }
    };
}
