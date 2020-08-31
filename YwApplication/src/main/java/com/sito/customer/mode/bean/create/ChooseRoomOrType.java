package com.sito.customer.mode.bean.create;

/**
 * Created by zhangan on 2017/10/9.
 */

public class ChooseRoomOrType {
    private String name;
    private long id;

    public ChooseRoomOrType() {
    }

    public ChooseRoomOrType(String name, long id) {
        this.name = name;
        this.id = id;
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
}
