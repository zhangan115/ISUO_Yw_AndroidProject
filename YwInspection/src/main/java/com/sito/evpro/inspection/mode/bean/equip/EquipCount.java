package com.sito.evpro.inspection.mode.bean.equip;

import java.util.List;

/**
 * 作者：Yangzb on 2017/7/1 11:37
 * 邮箱：yangzongbin@si-top.com
 */
public class EquipCount {
    private String place;
    private String count;
    private List<EquipName> mList;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<EquipName> getmList() {
        return mList;
    }

    public void setmList(List<EquipName> mList) {
        this.mList = mList;
    }
}
