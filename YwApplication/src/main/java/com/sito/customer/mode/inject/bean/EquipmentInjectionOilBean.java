package com.sito.customer.mode.inject.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sito.customer.mode.bean.User;

/**
 * 设备注油信息
 * Created by zhangan on 2018/4/13.
 */

public class EquipmentInjectionOilBean implements Parcelable {

    private int beforeOrBack;//1 前轴 2 后轴
    private long id;
    private long createTime;
    private long nextTime;
    private User user;

    public int getBeforeOrBack() {
        return beforeOrBack;
    }

    public void setBeforeOrBack(int beforeOrBack) {
        this.beforeOrBack = beforeOrBack;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class UserBean {
    }


    public EquipmentInjectionOilBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.beforeOrBack);
        dest.writeLong(this.id);
        dest.writeLong(this.createTime);
        dest.writeLong(this.nextTime);
        dest.writeParcelable(this.user, flags);
    }

    protected EquipmentInjectionOilBean(Parcel in) {
        this.beforeOrBack = in.readInt();
        this.id = in.readLong();
        this.createTime = in.readLong();
        this.nextTime = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<EquipmentInjectionOilBean> CREATOR = new Creator<EquipmentInjectionOilBean>() {
        @Override
        public EquipmentInjectionOilBean createFromParcel(Parcel source) {
            return new EquipmentInjectionOilBean(source);
        }

        @Override
        public EquipmentInjectionOilBean[] newArray(int size) {
            return new EquipmentInjectionOilBean[size];
        }
    };
}
