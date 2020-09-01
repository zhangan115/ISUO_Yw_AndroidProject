package com.isuo.yw2application.mode.bean.db;

import com.isuo.yw2application.app.Yw2Application;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Yangzb on 2017/7/10 09:34
 * E-mail：yangzongbin@si-top.com
 * 登录用户信息
 */
@Entity(nameInDb = "user_info")
public class UserInfo {

    @Id(autoincrement = true)
    private Long userId;
    private String userName = Yw2Application.getInstance().getUserName();
    private String passWord = Yw2Application.getInstance().getUserPass();

    @Generated(hash = 1231705238)
    public UserInfo(Long userId, String userName, String passWord) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
