package com.isuo.yw2application.mode.bean.resource;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2018/3/6.
 */

public class ResTree implements Parcelable{

    private Res resource;
    private boolean check = false;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Res getResource() {
        return resource;
    }

    public void setResource(Res resource) {
        this.resource = resource;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resource, flags);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
    }

    public ResTree() {
    }

    protected ResTree(Parcel in) {
        this.resource = in.readParcelable(Res.class.getClassLoader());
        this.check = in.readByte() != 0;
    }

    public static final Creator<ResTree> CREATOR = new Creator<ResTree>() {
        @Override
        public ResTree createFromParcel(Parcel source) {
            return new ResTree(source);
        }

        @Override
        public ResTree[] newArray(int size) {
            return new ResTree[size];
        }
    };
}
