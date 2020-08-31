package com.sito.customer.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2018/1/18.
 */

public class CustomerConfigBean implements Parcelable {

    private String configCode;
    private String configDesc;
    private long configId;
    private String configName;
    private String configValue;

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.configCode);
        dest.writeString(this.configDesc);
        dest.writeLong(this.configId);
        dest.writeString(this.configName);
        dest.writeString(this.configValue);
    }

    public CustomerConfigBean() {
    }

    protected CustomerConfigBean(Parcel in) {
        this.configCode = in.readString();
        this.configDesc = in.readString();
        this.configId = in.readLong();
        this.configName = in.readString();
        this.configValue = in.readString();
    }

    public static final Creator<CustomerConfigBean> CREATOR = new Creator<CustomerConfigBean>() {
        @Override
        public CustomerConfigBean createFromParcel(Parcel source) {
            return new CustomerConfigBean(source);
        }

        @Override
        public CustomerConfigBean[] newArray(int size) {
            return new CustomerConfigBean[size];
        }
    };
}
