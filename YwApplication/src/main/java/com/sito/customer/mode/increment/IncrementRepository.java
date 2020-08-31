package com.sito.customer.mode.increment;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sito.customer.api.Api;
import com.sito.customer.api.ApiCallBack;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.IListCallBack;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.UploadResult;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.ImageDao;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.customer.mode.bean.db.VoiceDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡检
 * Created by zhangan on 2018/4/3.
 */

public class IncrementRepository implements IncrementDataSource {

    private IncrementRepository(Context context) {

    }

    private static IncrementRepository repository;

    public static IncrementRepository getRepository(Context context) {
        if (repository == null) {
            repository = new IncrementRepository(context);
        }
        return repository;
    }

    private List<Image> images;

    @NonNull
    @Override
    public Subscription loadIncrementFromDb(final long workId, @NonNull final LoadIncrementDataCallBack callBack) {
        return CustomerApp.getInstance().getDaoSession()
                .getImageDao().queryBuilder()
                .where(ImageDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
                        , ImageDao.Properties.WorkType.eq(ConstantInt.INCREMENT_WORK)
                        , ImageDao.Properties.ItemId.eq(workId)).rx().list().doOnNext(new Action1<List<Image>>() {
                    @Override
                    public void call(List<Image> imageList) {
                        images = imageList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<Image>, Observable<Voice>>() {
                    @Override
                    public Observable<Voice> call(List<Image> imageList) {
                        return CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                                .where(VoiceDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
                                        , VoiceDao.Properties.WorkType.eq(ConstantInt.INCREMENT_WORK)
                                        , VoiceDao.Properties.ItemId.eq(workId)).rx().unique();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Voice>() {
                    @Override
                    public void call(Voice voice) {
                        callBack.onSuccess(images, voice);
                    }
                });
    }

    @NonNull
    @Override
    public Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        QueryBuilder qb = CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder();
        qb.where(VoiceDao.Properties.WorkType.eq(workType), VoiceDao.Properties.IsUpload.eq(false));
        final List<Voice> voices = qb.list();
        for (int i = 0; i < voices.size(); i++) {
            File file = new File(voices.get(i).getVoiceLocal());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file" + i, file.getName(), requestFile);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postVoiceFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> fileBeen) {
                callBack.onFinish();
                if (fileBeen == null || fileBeen.size() == 0) {
                    callBack.onError("上传失败!");
                } else {
                    callBack.onSuccess(fileBeen);
                    for (int i = 0; i < fileBeen.size(); i++) {
                        Voice voice = voices.get(i);
                        voice.setBackUrl(fileBeen.get(i));
                        CustomerApp.getInstance().getDaoSession().getVoiceDao().update(voice);
                    }
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("上传失败!");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription postIncrementVoiceFile(Voice voice, @NonNull String businessType, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        File file = new File(voice.getVoiceLocal());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postVoiceFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> fileBeen) {
                callBack.onFinish();
                if (fileBeen == null || fileBeen.size() == 0) {
                    callBack.onError("上传失败!");
                } else {
                    callBack.onSuccess(fileBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("上传失败!");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadIncrementData(String jsonStr, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(IncrementApi.class).finishIncrement(jsonStr);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String string) {
                callBack.onFinish();
                callBack.onSuccess(string);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId
            , @NonNull final Image image, @NonNull final UploadImageCallBack callBack) {
        image.setSaveTime(System.currentTimeMillis());
        image.setWorkType(workType);
        if (itemId != null) {
            image.setItemId(itemId);
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(image.getImageLocal());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                if (strings == null || strings.size() == 0) {
                    CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                    callBack.onError(image);
                } else {
                    image.setIsUpload(true);
                    image.setBackUrl(strings.get(0));
                    CustomerApp.getInstance().getDaoSession().getImageDao().insertOrReplaceInTx(image);
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                callBack.onFinish();
                callBack.onError(image);
            }
        }.execute1();
    }

    @Override
    public void saveVoice(Voice voice) {
        if (voice != null) {
            CustomerApp.getInstance().getDaoSession().getVoiceDao().rx().insertOrReplace(voice).subscribe();
        }
    }

    @Override
    public void cleanVoiceData(Voice voice) {
        if (voice != null) {
            CustomerApp.getInstance().getDaoSession().getVoiceDao().deleteInTx(voice);
        }
    }

    @Override
    public void cleanImageData(List<Image> imageList) {
        if (imageList != null) {
            CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(imageList);
        }
    }

    @Override
    public void cleanIncrementCache() {
        //更新图片状态为已提交
        List<Image> images = CustomerApp.getInstance().getDaoSession().getImageDao().queryBuilder()
                .where(ImageDao.Properties.WorkType.eq(ConstantInt.INCREMENT)).list();
        CustomerApp.getInstance().getDaoSession().getImageDao().deleteInTx(images);
        //更新录音为已提交
        List<Voice> voices = CustomerApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.INCREMENT)).list();
        CustomerApp.getInstance().getDaoSession().getVoiceDao().deleteInTx(voices);
    }

    @NonNull
    @Override
    public Subscription postIncrementInfo(JSONObject info, @NonNull final IObjectCallBack<UploadResult> callBack) {
        Observable<Bean<UploadResult>> observable = Api.createRetrofit().create(IncrementApi.class)
                .upLoadIncrement(info.toString());
        return new ApiCallBack<UploadResult>(observable) {
            @Override
            public void onSuccess(UploadResult uploadResult) {
                callBack.onSuccess(uploadResult);
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription startIncrement(long workId, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(IncrementApi.class).startIncrement(workId);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String str) {
                callBack.onFinish();
                callBack.onSuccess(str);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }
}
