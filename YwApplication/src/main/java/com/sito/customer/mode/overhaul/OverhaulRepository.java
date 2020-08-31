package com.sito.customer.mode.overhaul;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sito.customer.api.Api;
import com.sito.customer.api.ApiCallBack;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.common.ConstantInt;
import com.sito.customer.mode.Bean;
import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.UploadImageCallBack;
import com.sito.customer.mode.bean.db.Image;
import com.sito.customer.mode.bean.db.ImageDao;
import com.sito.customer.mode.bean.db.Voice;
import com.sito.customer.mode.bean.db.VoiceDao;
import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.customer.mode.bean.overhaul.OverhaulNoteBean;
import com.sito.customer.mode.bean.overhaul.WorkBean;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡检
 * Created by zhangan on 2018/3/20.
 */

public class OverhaulRepository implements OverhaulSourceData {

    private static OverhaulRepository repository;

    private OverhaulRepository(Context context) {

    }

    public static OverhaulRepository getRepository(Context context) {
        if (repository == null) {
            repository = new OverhaulRepository(context);
        }
        return repository;
    }


    @NonNull
    @Override
    public Subscription getSecureInfo(long securityId, @NonNull final IObjectCallBack<OverhaulNoteBean> callBack) {
        Observable<Bean<OverhaulNoteBean>> observable = Api.createRetrofit().create(OverhaulApi.class)
                .getOverhaulNote(securityId);
        return new ApiCallBack<OverhaulNoteBean>(observable) {
            @Override
            public void onSuccess(OverhaulNoteBean secureBean) {
                callBack.onFinish();
                if (secureBean != null && secureBean.getPageList() != null
                        && secureBean.getPageList().size() > 0) {
                    callBack.onSuccess(secureBean);
                } else {
                    callBack.onError("没有数据");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("没有数据");
            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription startRepair(String repairId) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(OverhaulApi.class).startOverhaul(repairId);
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String str) {

            }

            @Override
            public void onFail() {

            }
        }.execute1();
    }

    @NonNull
    @Override
    public Subscription getRepairWork(@NonNull String repairId, @NonNull final IObjectCallBack<OverhaulBean> callBack) {
        Observable<Bean<OverhaulBean>> observable = Api.createRetrofit().create(OverhaulApi.class)
                .getRepairWork(repairId);
        return new ApiCallBack<OverhaulBean>(observable) {
            @Override
            public void onSuccess(OverhaulBean data) {
                callBack.onFinish();
                callBack.onSuccess(data);
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
    public Subscription loadRepairWorkFromDb(@NonNull final String repairId, @NonNull final IObjectCallBack<WorkBean> callBack) {
        return Observable.just(new WorkBean())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<WorkBean, Observable<WorkBean>>() {
                    @Override
                    public Observable<WorkBean> call(WorkBean workBean) {
                        List<Image> images = CustomerApp.getInstance().getDaoSession().getImageDao()
                                .queryBuilder().where(ImageDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
                                        , ImageDao.Properties.WorkType.eq(ConstantInt.CHECKPAIR)
                                        , ImageDao.Properties.ItemId.eq(Long.valueOf(repairId)))
                                .list();
                        if (images != null) {
                            workBean.setImages(images);
                        }
                        try {
                            Voice voice = CustomerApp.getInstance().getDaoSession().getVoiceDao()
                                    .queryBuilder().where(VoiceDao.Properties.CurrentUserId.eq(CustomerApp.getInstance().getCurrentUser().getUserId())
                                            , VoiceDao.Properties.ItemId.eq(Long.valueOf(repairId))
                                            , VoiceDao.Properties.WorkType.eq(ConstantInt.CHECKPAIR))
                                    .unique();
                            if (voice != null) {
                                workBean.setVoice(voice);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return Observable.just(workBean);
                    }
                })
                .subscribe(new Subscriber<WorkBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFinish();
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(WorkBean workBean) {
                        callBack.onFinish();
                        callBack.onSuccess(workBean);
                    }
                });
    }

    @NonNull
    @Override
    public Subscription uploadOverhaulData(@NonNull JSONObject jsonObject, @NonNull final UploadRepairDataCallBack callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(OverhaulApi.class).uploadRepairWork(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(String s) {
                callBack.uploadSuccess();
            }

            @Override
            public void onFail() {
                callBack.uploadFail();
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

    @NonNull
    @Override
    public Subscription uploadFile(@NonNull String filePath, @NonNull String businessType
            , @NonNull String fileType, @NonNull final UploadFileCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("businessType", businessType)
                .addFormDataPart("fileType", fileType);
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), requestFile);
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<Bean<List<String>>> observable = Api.createRetrofit().create(Api.File.class)
                .postImageFile(parts);
        return new ApiCallBack<List<String>>(observable) {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    callBack.uploadSuccess(strings.get(0));
                } else {
                    callBack.uploadFail();
                }
            }

            @Override
            public void onFail() {
                callBack.uploadFail();
            }
        }.execute1();
    }
}