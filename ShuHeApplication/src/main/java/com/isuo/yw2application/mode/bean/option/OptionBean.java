package com.isuo.yw2application.mode.bean.option;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/5 17:05
 * E-mail：yangzongbin@si-top.com
 * 字典
 */
public class OptionBean implements Parcelable  {

    private int optionId;
    private String optionName;
    private List<ItemListBean> itemList;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }


    public static class ItemListBean implements Parcelable {

        private int itemApply;
        private String itemCode;
        private int itemId;
        private String itemName;

        public int getItemApply() {
            return itemApply;
        }

        public void setItemApply(int itemApply) {
            this.itemApply = itemApply;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.itemApply);
            dest.writeString(this.itemCode);
            dest.writeInt(this.itemId);
            dest.writeString(this.itemName);
        }

        public ItemListBean() {
        }

        protected ItemListBean(Parcel in) {
            this.itemApply = in.readInt();
            this.itemCode = in.readString();
            this.itemId = in.readInt();
            this.itemName = in.readString();
        }

        public static final Creator<ItemListBean> CREATOR = new Creator<ItemListBean>() {
            @Override
            public ItemListBean createFromParcel(Parcel source) {
                return new ItemListBean(source);
            }

            @Override
            public ItemListBean[] newArray(int size) {
                return new ItemListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.optionId);
        dest.writeString(this.optionName);
        dest.writeTypedList(this.itemList);
    }

    public OptionBean() {
    }

    protected OptionBean(Parcel in) {
        this.optionId = in.readInt();
        this.optionName = in.readString();
        this.itemList = in.createTypedArrayList(ItemListBean.CREATOR);
    }

    public static final Creator<OptionBean> CREATOR = new Creator<OptionBean>() {
        @Override
        public OptionBean createFromParcel(Parcel source) {
            return new OptionBean(source);
        }

        @Override
        public OptionBean[] newArray(int size) {
            return new OptionBean[size];
        }
    };
}
