package com.sito.customer.mode.bean.work;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 工作item
 * Created by zhangan on 2018/3/26.
 */

public class WorkItem implements Parcelable {


    public WorkItem(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    private int id;
    private String name;
    private int icon;
    private boolean isAdd;
    private boolean isEdit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public WorkItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.icon);
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
    }

    protected WorkItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readInt();
        this.isAdd = in.readByte() != 0;
        this.isEdit = in.readByte() != 0;
    }

    public static final Creator<WorkItem> CREATOR = new Creator<WorkItem>() {
        @Override
        public WorkItem createFromParcel(Parcel source) {
            return new WorkItem(source);
        }

        @Override
        public WorkItem[] newArray(int size) {
            return new WorkItem[size];
        }
    };
}
