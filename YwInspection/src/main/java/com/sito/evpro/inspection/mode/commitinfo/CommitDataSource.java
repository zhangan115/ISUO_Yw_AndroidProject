package com.sito.evpro.inspection.mode.commitinfo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.Voice;
import com.sito.evpro.inspection.mode.bean.fault.DefaultFlowBean;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * 接口定义M层
 * Created by zhangan on 2017-06-22.
 */

public interface CommitDataSource {

    //增值工作
    @NonNull
    Subscription postIncrementInfo(long sourceId, long workType, @NonNull String workContent, @NonNull String workSound, @NonNull String workImages, @NonNull String soundTimescale, long equipId, int operation, @NonNull IObjectCallBack<UploadResult> callBack);

    /**
     * 提交专项工作
     *
     * @param info     参数
     * @param callBack 回调
     * @return 订阅
     */
    @NonNull
    Subscription postIncrementInfo(JSONObject info, @NonNull IObjectCallBack<UploadResult> callBack);

    //提交图片文件
    @NonNull
    Subscription postIncrementImageFile(int workType, @NonNull String businessType, @NonNull IListCallBack<String> callBack);

    //提交录音文件
    @NonNull
    Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull IListCallBack<String> callBack);

    //故障上报
    @NonNull
    Subscription postFaultInfo(JSONObject jsonObject, @NonNull IObjectCallBack<UploadResult> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription postImageFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull IListCallBack<String> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, @NonNull Image image, @NonNull UploadImageCallBack callBack);


    //拍完照 直接上传
    @NonNull
    Subscription postImageFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull IObjectCallBack<Image> callBack);

    //拍完照 直接上传
    @NonNull
    Subscription postImageFile(int workType, Long itemId, @NonNull String businessType, @NonNull String path, @NonNull IObjectCallBack<Image> callBack);

    //录完音 直接上传
    @NonNull
    Subscription postVoiceFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull IListCallBack<String> callBack);

    //获取固化流程
    @NonNull
    Subscription getDefaultFlow(@NonNull IListCallBack<DefaultFlowBean> callBack);

    void cleanIncrementCache();

    Subscription uploadIncrementData(String jsonStr, @NonNull IObjectCallBack<String> callBack);

    Subscription loadIncrementFromDb(long workId, @NonNull LoadIncrementDataCallBack callBack);

    Subscription postIncrementVoiceFile(Voice voice, @NonNull String businessType, @NonNull final IListCallBack<String> callBack);

    interface LoadIncrementDataCallBack {
        void onSuccess(List<Image> imageList, Voice voice);
    }

    void saveVoice(Voice voice);

    void cleanVoiceData(Voice voice);

    void cleanImageData(List<Image> imageList);
}
