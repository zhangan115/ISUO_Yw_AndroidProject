package com.isuo.yw2application.mode.bean;

/**
 * Created by zhangan on 2017-07-25.
 */

public class Register {
    String phoneNum;
    String password;
    String VerificationCode;//验证码

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }
}
