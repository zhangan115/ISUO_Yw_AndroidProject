package com.sito.evpro.inspection.mode.bean.overhaul;

/**
 * Created by zhangan on 2017-07-21.
 */

public class RepairUsers {
    private long id;
    private UserBean user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean userBean) {
        this.user = userBean;
    }
}
