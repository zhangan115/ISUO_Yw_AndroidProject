package com.isuo.yw2application.mode.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2018/4/9.
 */

public class CheckListBean implements Parcelable {

    private long checkId;
    private String checkName;
    private String value;

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.checkId);
        dest.writeString(this.checkName);
        dest.writeString(this.value);
    }

    public CheckListBean() {
    }

    protected CheckListBean(Parcel in) {
        this.checkId = in.readLong();
        this.checkName = in.readString();
        this.value = in.readString();
    }

    public static final Creator<CheckListBean> CREATOR = new Creator<CheckListBean>() {
        @Override
        public CheckListBean createFromParcel(Parcel source) {
            return new CheckListBean(source);
        }

        @Override
        public CheckListBean[] newArray(int size) {
            return new CheckListBean[size];
        }
    };
}
