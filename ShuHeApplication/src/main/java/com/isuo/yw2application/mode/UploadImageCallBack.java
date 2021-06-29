package com.isuo.yw2application.mode;


import com.isuo.yw2application.mode.bean.db.Image;

/**
 * 照片上传回调
 * Created by zhangan on 2017/11/15.
 */

public interface UploadImageCallBack {

    void onSuccess();

    void onFinish();

    void onError(Image image);
}
