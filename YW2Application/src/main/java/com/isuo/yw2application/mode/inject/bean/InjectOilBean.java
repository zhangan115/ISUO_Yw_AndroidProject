package com.isuo.yw2application.mode.inject.bean;

import java.util.List;

/**
 * 注油item
 * Created by zhangan on 2018/4/13.
 */

public class InjectOilBean {

    private int isEnabled;
    private int isRequeired;
    private long itemId;
    private String itemName;
    private int itemType;
    private String valueUnit;
    private String value;
    private List<InjectOilOptionListBean> optionList;

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getIsRequeired() {
        return isRequeired;
    }

    public void setIsRequeired(int isRequeired) {
        this.isRequeired = isRequeired;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<InjectOilOptionListBean> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<InjectOilOptionListBean> optionList) {
        this.optionList = optionList;
    }
}
