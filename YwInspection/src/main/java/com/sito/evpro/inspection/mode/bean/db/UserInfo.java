package com.sito.evpro.inspection.mode.bean.db;

import com.sito.evpro.inspection.app.InspectionApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Yangzb on 2017/7/10 09:34
 * E-mail：yangzongbin@si-top.com
 * 登录用户信息
 */
@Entity(nameInDb = "file_login")
public class UserInfo {
    @Id
    private Long userId;
    private String userName = InspectionApp.getInstance().getUserName();
    private String passward = InspectionApp.getInstance().getUserPass();

    @Generated(hash = 2114391641)
    public UserInfo(Long userId, String userName, String passward) {
        this.userId = userId;
        this.userName = userName;
        this.passward = passward;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}
