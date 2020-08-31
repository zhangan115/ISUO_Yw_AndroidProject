package com.sito.customer.mode.increment;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.UploadResult;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.Voice;

import org.json.JSONObject;

import java.util.List;

import rx.Subscription;

/**
 * Created by zhangan on 2018/4/3.
 */

public interface IncrementDataSource {

    interface LoadIncrementDataCallBack {
        void onSuccess(List<Image> imageList, Voice voice);
    }

    @NonNull
    Subscription loadIncrementFromDb(long workId, @NonNull LoadIncrementDataCallBack callBack);

    //提交录音文件
    @NonNull
    Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull IListCallBack<String> callBack);

    @NonNull
    Subscription postIncrementVoiceFile(Voice voice, @NonNull String businessType, @NonNull final IListCallBack<String> callBack);

    @NonNull
    Subscription uploadIncrementData(String jsonStr, @NonNull IObjectCallBack<String> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, @NonNull Image image, @NonNull UploadImageCallBack callBack);

    void saveVoice(Voice voice);

    void cleanVoiceData(Voice voice);

    void cleanImageData(List<Image> imageList);

    void cleanIncrementCache();

    /**
     * 提交专项工作
     *
     * @param info     参数
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription postIncrementInfo(JSONObject info, @NonNull IObjectCallBack<UploadResult> callBack);

    @NonNull
    Subscription startIncrement(long workId, @NonNull IObjectCallBack<String> callBack);

}
