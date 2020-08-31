package com.sito.evpro.inspection.mode.commitinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.api.Api;
import com.sito.evpro.inspection.api.ApiCallBack;
import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.common.ConstantInt;
import com.sito.evpro.inspection.common.ConstantStr;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.IListCallBack;
import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.UploadImageCallBack;
import com.sito.evpro.inspection.mode.bean.db.Image;
import com.sito.evpro.inspection.mode.bean.db.ImageDao;
import com.sito.evpro.inspection.mode.bean.db.Voice;
import com.sito.evpro.inspection.mode.bean.db.VoiceDao;
import com.sito.evpro.inspection.mode.bean.fault.DefaultFlowBean;
import com.sito.evpro.inspection.mode.bean.upload.UploadResult;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡检版
 * Created by zhangan on 2017-06-22.
 */
@Singleton
public class CommitRepository implements CommitDataSource {

    private SharedPreferences sp;
    private long WELCOME_TIME = 1500;

    @Inject
    public CommitRepository(@NonNull Context context) {
        sp = context.getSharedPreferences(ConstantStr.USER_DATA, Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public Subscription postIncrementInfo(long sourceId, long workType, @NonNull String workContent, @NonNull String workSound, @NonNull String workImages, @NonNull String soundTimescale, long equipid, int operation, @NonNull final IObjectCallBack<UploadResult> callBack) {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("workIssued", 0);
            jsonRequest.put("workType", workType);
            jsonRequest.put("workContent", workContent);
            jsonRequest.put("workSound", workSound);
            jsonRequest.put("workImages", workImages);
            jsonRequest.put("soundTimescale", soundTimescale);
            if (equipid != -1) {
                jsonRequest.put("equipmentId", equipid);
                jsonRequest.put("operation", operation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<Bean<UploadResult>> observable = Api.createRetrofit().create(Api.Increment.class)
                .upLoadIncrement(jsonRequest.toString());
        return new ApiCallBack<UploadResult>(observable) {
            @Override
            public void onSuccess(UploadResult uploadResult) {
                callBack.onFinish();
                callBack.onSuccess(uploadResult);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    @NonNull
    @Override
    public Subscription postIncrementInfo(JSONObject info, @NonNull final IObjectCallBack<UploadResult> callBack) {
        Observable<Bean<UploadResult>> observable = Api.createRetrofit().create(Api.Increment.class)
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
        }.execute().subscribe();
    }

    @Override
    public Subscription postIncrementImageFile(final int workType, @NonNull String businessType, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        QueryBuilder qb = InspectionApp.getInstance().getDaoSession().getImageDao().queryBuilder();
        qb.where(ImageDao.Properties.WorkType.eq(workType), ImageDao.Properties.IsUpload.eq(false));
        final List<Image> images = qb.list();
        final List<String> urlS = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            urlS.add(images.get(i).getImageLocal());
            File file = new File(images.get(i).getImageLocal());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file" + i, file.getName(), requestFile);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> fileBeen) {
                callBack.onSuccess(fileBeen);
                for (int i = 0; i < fileBeen.size(); i++) {
                    Image image = images.get(i);
                    image.setBackUrl(fileBeen.get(i));
                    InspectionApp.getInstance().getDaoSession().getImageDao().update(image);
                }
            }

            @Override
            public void onFail() {
                callBack.onError("");
            }
        }.execute().subscribe();
    }

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
        }.execute().subscribe();
    }

    @Override
    public Subscription postIncrementVoiceFile(int workType, @NonNull String businessType, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        QueryBuilder qb = InspectionApp.getInstance().getDaoSession().getVoiceDao().queryBuilder();
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
                        InspectionApp.getInstance().getDaoSession().getVoiceDao().update(voice);
                    }
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("上传失败!");
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription postFaultInfo(JSONObject jsonObject, @NonNull final IObjectCallBack<UploadResult> callBack) {
        Observable<Bean<UploadResult>> observable = Api.createRetrofit().create(Api.Fault.class)
                .upLoadFault(jsonObject.toString());
        return new ApiCallBack<UploadResult>(observable) {
            @Override
            public void onSuccess(UploadResult uploadResult) {
                callBack.onFinish();
                callBack.onSuccess(uploadResult);

            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription postImageFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        QueryBuilder qb = InspectionApp.getInstance().getDaoSession().getImageDao().queryBuilder();
        qb.where(ImageDao.Properties.WorkType.eq(workType), ImageDao.Properties.IsUpload.eq(false));
        final Image image = (Image) qb.list().get(0);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                callBack.onSuccess(strings);
                String url = image.getBackUrl() + "," + strings.get(0);
                image.setBackUrl(url);
                InspectionApp.getInstance().getDaoSession().getImageDao().insertOrReplace(image);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription uploadImageFile(int workType, @NonNull String businessType, Long itemId, final @NonNull Image image
            , final @NonNull UploadImageCallBack callBack) {
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
                    InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                    callBack.onError(image);
                } else {
                    image.setIsUpload(true);
                    image.setBackUrl(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getImageDao().insertOrReplaceInTx(image);
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                callBack.onFinish();
                callBack.onError(image);
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription postImageFile(int workType, Long itemId, @NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<Image> callBack) {
        final Image image = new Image();
        image.setImageLocal(path);
        image.setSaveTime(System.currentTimeMillis());
        image.setWorkType(workType);
        if (itemId != null) {
            image.setItemId(itemId);
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "image");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings == null || strings.size() == 0) {
                    callBack.onError("上传失败");
                } else {
                    image.setIsUpload(true);
                    image.setBackUrl(strings.get(0));
                    InspectionApp.getInstance().getDaoSession().getImageDao().insertInTx(image);
                    callBack.onSuccess(image);
                }
                callBack.onFinish();
            }

            @Override
            public void onFail() {
                InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(image);
                callBack.onFinish();
                callBack.onError("上传失败");
            }
        }.execute().subscribe();
    }


    @NonNull
    @Override
    public Subscription postImageFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull final IObjectCallBack<Image> callBack) {
        return postImageFile(workType, null, businessType, path, callBack);
    }

    @NonNull
    @Override
    public Subscription postVoiceFile(int workType, @NonNull String businessType, @NonNull String path, @NonNull final IListCallBack<String> callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", "voice");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postVoiceFile(parts);
        QueryBuilder qb = InspectionApp.getInstance().getDaoSession().getVoiceDao().queryBuilder();
        qb.where(VoiceDao.Properties.WorkType.eq(workType), VoiceDao.Properties.IsUpload.eq(false));
        final Voice voice = (Voice) qb.list().get(0);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                callBack.onFinish();
                callBack.onSuccess(strings);
                String url = strings.get(0);
                voice.setBackUrl(url);
                InspectionApp.getInstance().getDaoSession().getVoiceDao().insertOrReplace(voice);
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @NonNull
    @Override
    public Subscription getDefaultFlow(@NonNull final IListCallBack<DefaultFlowBean> callBack) {
        Observable<Bean<List<DefaultFlowBean>>> observable = Api.createRetrofit().create(Api.Fault.class).getDefaultFlow(1);
        return new ApiCallBack<List<DefaultFlowBean>>(observable) {
            @Override
            public void onSuccess(List<DefaultFlowBean> defaultFlowBeen) {
                callBack.onFinish();
                if (defaultFlowBeen != null && defaultFlowBeen.size() > 0) {
                    callBack.onSuccess(defaultFlowBeen);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute().subscribe();
    }

    @Override
    public void cleanIncrementCache() {
        //更新图片状态为已提交
        List<Image> images = InspectionApp.getInstance().getDaoSession().getImageDao().queryBuilder()
                .where(ImageDao.Properties.WorkType.eq(ConstantInt.INCREMENT)).list();
        InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(images);
        //更新录音为已提交
        List<Voice> voices = InspectionApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                .where(VoiceDao.Properties.WorkType.eq(ConstantInt.INCREMENT)).list();
        InspectionApp.getInstance().getDaoSession().getVoiceDao().deleteInTx(voices);
        InspectionApp.getInstance().getWorkTypeMap().put(InspectionApp.getInstance().getCurrentUser().getUserId(), "");
        InspectionApp.getInstance().getWorkTypeIdMap().put(InspectionApp.getInstance().getCurrentUser().getUserId(), (long) -1);
        InspectionApp.getInstance().getWorkSourceMap().put(InspectionApp.getInstance().getCurrentUser().getUserId(), "");
        InspectionApp.getInstance().getWorkSourceIdMap().put(InspectionApp.getInstance().getCurrentUser().getUserId(), (long) -1);
    }

    @Override
    public Subscription uploadIncrementData(String jsonStr, @NonNull final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(Api.Increment.class).finishIncrement(jsonStr);
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
        }.execute().subscribe();
    }

    private List<Image> images;

    @Override
    public Subscription loadIncrementFromDb(final long workId, @NonNull final LoadIncrementDataCallBack callBack) {
        return InspectionApp.getInstance().getDaoSession()
                .getImageDao().queryBuilder()
                .where(ImageDao.Properties.CurrentUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
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
                        return InspectionApp.getInstance().getDaoSession().getVoiceDao().queryBuilder()
                                .where(VoiceDao.Properties.CurrentUserId.eq(InspectionApp.getInstance().getCurrentUser().getUserId())
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

    @Override
    public void saveVoice(Voice voice) {
        if (voice != null) {
            InspectionApp.getInstance().getDaoSession().getVoiceDao().rx().insertOrReplace(voice).subscribe();
        }
    }

    @Override
    public void cleanVoiceData(Voice voice) {
        if (voice != null) {
            InspectionApp.getInstance().getDaoSession().getVoiceDao().deleteInTx(voice);
        }
    }

    @Override
    public void cleanImageData(List<Image> imageList) {
        if (imageList != null) {
            InspectionApp.getInstance().getDaoSession().getImageDao().deleteInTx(imageList);
        }
    }
}
