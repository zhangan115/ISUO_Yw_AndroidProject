package com.isuo.yw2application.mode.equipment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.common.ConstantInt;
import com.isuo.yw2application.common.ConstantStr;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.check.CheckValue;
import com.isuo.yw2application.mode.bean.check.FaultList;
import com.isuo.yw2application.mode.bean.check.InspectionData;
import com.isuo.yw2application.mode.bean.equip.EquipRecordDetail;
import com.isuo.yw2application.mode.bean.equip.TimeLineBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;

/**
 * 设备
 * Created by zhangan on 2017/10/13.
 */
@Singleton
public class EquipmentRepository implements EquipmentDataSource {

    @Inject
    public EquipmentRepository() {
    }

    @Override
    public Subscription getOverByEId(long equipId, @NonNull final IListCallBack<OverhaulBean> callBack) {
        Observable<Bean<List<OverhaulBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getOverByEId(equipId, ConstantInt.PAGE_SIZE);
        return new ApiCallBack<List<OverhaulBean>>(observable) {
            @Override
            public void onSuccess(@Nullable List<OverhaulBean> overhaulBeen) {
                callBack.onFinish();
                if (overhaulBeen == null || overhaulBeen.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(overhaulBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getMoreOverByEId(long equipId, int lastId, @NonNull final IListCallBack<OverhaulBean> callBack) {
        Observable<Bean<List<OverhaulBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getMoreOverByEId(equipId, ConstantInt.PAGE_SIZE, lastId);
        return new ApiCallBack<List<OverhaulBean>>(observable) {
            @Override
            public void onSuccess(@Nullable List<OverhaulBean> overhaulBeen) {
                callBack.onFinish();
                if (overhaulBeen == null || overhaulBeen.size() == 0) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(overhaulBeen);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }


    @Override
    public Subscription getCheckData(long equipId, @NonNull final IObjectCallBack<InspectionData> callBack) {
        Observable<Bean<InspectionData>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getCheckData(equipId);
        return new ApiCallBack<InspectionData>(observable) {
            @Override
            public void onSuccess(@Nullable InspectionData checkDatas) {
                callBack.onFinish();
                if (checkDatas == null) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(checkDatas);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getCheckValue(long equipId, int inspecId, @NonNull final IObjectCallBack<CheckValue> callBack) {
        Observable<Bean<CheckValue>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getCheckValue(equipId, inspecId);
        return new ApiCallBack<CheckValue>(observable) {
            @Override
            public void onSuccess(@Nullable CheckValue checkValue) {
                callBack.onFinish();
                if (checkValue == null) {
                    callBack.onError("");
                } else {
                    callBack.onSuccess(checkValue);
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    @Override
    public Subscription getFaultByEId(long equipId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getFaultByEId(equipId, ConstantInt.PAGE_SIZE);
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");

            }
        }.execute1();
    }

    @Override
    public Subscription getMoreFaultByEId(long equipId, int lastId, final IListCallBack<FaultList> callBack) {
        Observable<Bean<List<FaultList>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getMoreFaultByEId(ConstantInt.PAGE_SIZE, equipId, lastId);
        return new ApiCallBack<List<FaultList>>(observable) {
            @Override
            public void onSuccess(@Nullable List<FaultList> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
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
    public Subscription getEquipRepairRecordData(long equipmentId, final IListCallBack<TimeLineBean> callBack) {
        Observable<Bean<List<TimeLineBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getTimeLineData(ConstantInt.MAX_PAGE_SIZE, equipmentId, ConstantInt.TYPE_REPAIR);
        return new ApiCallBack<List<TimeLineBean>>(observable) {
            @Override
            public void onSuccess(@Nullable List<TimeLineBean> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
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
    public Subscription getEquipCheckData(long equipmentId, final IListCallBack<TimeLineBean> callBack) {
        Observable<Bean<List<TimeLineBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getTimeLineData(ConstantInt.MAX_PAGE_SIZE, equipmentId, ConstantInt.TYPE_CHECK);
        return new ApiCallBack<List<TimeLineBean>>(observable) {
            @Override
            public void onSuccess(@Nullable List<TimeLineBean> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
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
    public Subscription getEquipExperimentData(long equipmentId, final IListCallBack<TimeLineBean> callBack) {
        Observable<Bean<List<TimeLineBean>>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getTimeLineData(ConstantInt.MAX_PAGE_SIZE, equipmentId, ConstantInt.TYPE_EXPERIMENT);
        return new ApiCallBack<List<TimeLineBean>>(observable) {
            @Override
            public void onSuccess(@Nullable List<TimeLineBean> faultLists) {
                callBack.onFinish();
                if (faultLists != null && faultLists.size() > 0) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
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
    public Subscription getEquipRecordData(long equipmentRecordId, final IObjectCallBack<EquipRecordDetail> callBack) {
        Observable<Bean<EquipRecordDetail>> observable = Api.createRetrofit().create(Api.Equip.class)
                .getRecordData(equipmentRecordId);
        return new ApiCallBack<EquipRecordDetail>(observable) {
            @Override
            public void onSuccess(@Nullable EquipRecordDetail faultLists) {
                callBack.onFinish();
                if (faultLists != null) {
                    callBack.onSuccess(faultLists);
                } else {
                    callBack.onError("");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");

            }
        }.execute1();
    }

    @Override
    public void downLoadFile(final String fileName, final String filePath, String downLoadUrl, @NonNull final DownLoadCallBack callBack) {
        final DownLoadHandle downLoadHandle = new DownLoadHandle(callBack);
        Call<ResponseBody> call = Api.createRetrofit().create(Api.Equip.class).downloadFile(downLoadUrl);
        if (!new File(filePath).exists()) {
            new File(filePath).mkdir();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccess = writeResponseBodyToDisk(filePath, fileName, response.body());
                            if (isSuccess) {
                                Message message = new Message();
                                message.what = 0;
                                Bundle bundle = new Bundle();
                                bundle.putString(ConstantStr.KEY_BUNDLE_STR, fileName);
                                bundle.putString(ConstantStr.KEY_BUNDLE_STR_1, filePath);
                                message.setData(bundle);
                                downLoadHandle.sendMessage(message);
                            } else {
                                downLoadHandle.sendEmptyMessage(1);
                            }
                        }
                    }).start();
                } else {
                    downLoadHandle.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                downLoadHandle.sendEmptyMessage(1);
            }
        });

    }

    private boolean writeResponseBodyToDisk(String filePath, String fileName, ResponseBody body) {
        try {
            File futureStudioIconFile = new File(filePath + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static class DownLoadHandle extends Handler {

        private DownLoadCallBack callBack;

        DownLoadHandle(DownLoadCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String fileName = msg.getData().getString(ConstantStr.KEY_BUNDLE_STR);
                String filePath = msg.getData().getString(ConstantStr.KEY_BUNDLE_STR_1);
                callBack.onSuccess(fileName, filePath);
            } else {
                callBack.onFile();
            }
        }
    }


}
