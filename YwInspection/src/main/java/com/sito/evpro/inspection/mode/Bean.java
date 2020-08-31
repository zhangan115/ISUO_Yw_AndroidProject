package com.sito.evpro.inspection.mode;

/**
 * 网络请求实体类
 * Created by zhangan on 2016-08-30.
 */
public class Bean<T> {

    private T data;
    private int errorCode;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


}
