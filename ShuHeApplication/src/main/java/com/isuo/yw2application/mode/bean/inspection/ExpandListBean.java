package com.isuo.yw2application.mode.bean.inspection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 设备的自定义数据
 * Created by zhangan on 2018/1/9.
 */

public class ExpandListBean implements Parcelable {

    private long expandId;
    private String fieldCode;
    private String fieldName;
    private int fieldType;
    private String fieldUnit;
    private int isRequired;
    private int useStatus;
    private String value;

    public long getExpandId() {
        return expandId;
    }

    public void setExpandId(long expandId) {
        this.expandId = expandId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldUnit() {
        return fieldUnit;
    }

    public void setFieldUnit(String fieldUnit) {
        this.fieldUnit = fieldUnit;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
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
        dest.writeLong(this.expandId);
        dest.writeString(this.fieldCode);
        dest.writeString(this.fieldName);
        dest.writeInt(this.fieldType);
        dest.writeString(this.fieldUnit);
        dest.writeInt(this.isRequired);
        dest.writeInt(this.useStatus);
        dest.writeString(this.value);
    }

    public ExpandListBean() {
    }

    protected ExpandListBean(Parcel in) {
        this.expandId = in.readLong();
        this.fieldCode = in.readString();
        this.fieldName = in.readString();
        this.fieldType = in.readInt();
        this.fieldUnit = in.readString();
        this.isRequired = in.readInt();
        this.useStatus = in.readInt();
        this.value = in.readString();
    }

    public static final Creator<ExpandListBean> CREATOR = new Creator<ExpandListBean>() {
        @Override
        public ExpandListBean createFromParcel(Parcel source) {
            return new ExpandListBean(source);
        }

        @Override
        public ExpandListBean[] newArray(int size) {
            return new ExpandListBean[size];
        }
    };
}
