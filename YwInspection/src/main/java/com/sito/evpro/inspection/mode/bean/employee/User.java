package com.sito.evpro.inspection.mode.bean.employee;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2017-07-07.
 */

public class User implements Parcelable {

    private long userId;
    private String realName;
    private String userName;
    private int userType;
    private String userPhone;
    private String userRoleNames;
    private String portraitUrl;

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

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.realName);
        dest.writeString(this.userName);
        dest.writeInt(this.userType);
        dest.writeString(this.userPhone);
        dest.writeString(this.userRoleNames);
        dest.writeString(this.portraitUrl);
    }

    protected User(Parcel in) {
        this.userId = in.readLong();
        this.realName = in.readString();
        this.userName = in.readString();
        this.userType = in.readInt();
        this.userPhone = in.readString();
        this.userRoleNames = in.readString();
        this.portraitUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
