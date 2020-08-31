package com.sito.evpro.inspection.mode;

import com.sito.evpro.inspection.mode.bean.db.Image;

/**
 * 照片上传回调
 * Created by zhangan on 2017/11/15.
 */

public interface UploadImageCallBack {

    void onSuccess();

    void onFinish();

    void onError(Image image);
}
