package com.isuo.yw2application.mode.bean.resource;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangan on 2018/3/6.
 */

public class Res implements Parcelable {

    private Long resourceId;
    private String resourceName;
    private String resourceUrl;
    private int menuType;
    private Long parentResourceId;
    private String menuImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.resourceId);
        dest.writeString(this.resourceName);
        dest.writeString(this.resourceUrl);
        dest.writeInt(this.menuType);
        dest.writeValue(this.parentResourceId);
        dest.writeString(this.menuImage);
    }

    public Res() {
    }

    protected Res(Parcel in) {
        this.resourceId = (Long) in.readValue(Long.class.getClassLoader());
        this.resourceName = in.readString();
        this.resourceUrl = in.readString();
        this.menuType = in.readInt();
        this.parentResourceId = (Long) in.readValue(Long.class.getClassLoader());
        this.menuImage = in.readString();
    }

    public static final Creator<Res> CREATOR = new Creator<Res>() {
        @Override
        public Res createFromParcel(Parcel source) {
            return new Res(source);
        }

        @Override
        public Res[] newArray(int size) {
            return new Res[size];
        }
    };

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public Long getParentResourceId() {
        return parentResourceId;
    }

    public void setParentResourceId(Long parentResourceId) {
        this.parentResourceId = parentResourceId;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }
}
