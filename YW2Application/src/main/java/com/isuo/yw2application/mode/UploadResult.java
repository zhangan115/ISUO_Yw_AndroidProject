package com.isuo.yw2application.mode;

/**
 * 上传成功
 * Created by zhangan on 2017-04-17.
 */

public class UploadResult {

    private int errorCode;
    private String message;

    public UploadResult() {
    }

    public UploadResult(int errorCode) {
        this.errorCode = errorCode;
    }

    public UploadResult(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
