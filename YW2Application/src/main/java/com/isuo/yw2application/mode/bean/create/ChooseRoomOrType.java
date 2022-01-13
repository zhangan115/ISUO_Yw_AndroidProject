package com.isuo.yw2application.mode.bean.create;

import java.util.List;

/**
 * Created by zhangan on 2017/10/9.
 */

public class ChooseRoomOrType {

    private String name;
    private long id;
    private long parentId;
    private boolean isSelect;
    private int level;

    private List<ChooseRoomOrType> chooseRoomOrTypeList;

    public ChooseRoomOrType() {
    }

    public ChooseRoomOrType(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<ChooseRoomOrType> getChooseRoomOrTypeList() {
        return chooseRoomOrTypeList;
    }

    public void setChooseRoomOrTypeList(List<ChooseRoomOrType> chooseRoomOrTypeList) {
        this.chooseRoomOrTypeList = chooseRoomOrTypeList;
    }
}
