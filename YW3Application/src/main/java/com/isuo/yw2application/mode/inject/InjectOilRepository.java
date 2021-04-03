package com.isuo.yw2application.mode.inject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.isuo.yw2application.api.Api;
import com.isuo.yw2application.api.ApiCallBack;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.IListCallBack;
import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.inject.bean.InjectEquipment;
import com.isuo.yw2application.mode.inject.bean.InjectEquipmentLog;
import com.isuo.yw2application.mode.inject.bean.InjectItemBean;
import com.isuo.yw2application.mode.inject.bean.InjectRoomBean;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 注油
 * Created by zhangan on 2018/4/9.
 */

public class InjectOilRepository implements InjectOilDataSource {

    private static InjectOilRepository repository;

    private InjectOilRepository(Context context) {

    }

    public static InjectOilRepository getRepository(Context context) {
        if (repository == null) {
            repository = new InjectOilRepository(context);
        }
        return repository;
    }

    @NonNull
    @Override
    public Subscription getInjectRoomList(@NonNull final IListCallBack<InjectRoomBean> callBack) {
        Observable<Bean<List<InjectRoomBean>>> observable = Api.createRetrofit().create(Api.InjectEquipmentApi.class).getInjectRoom(1);
        return new ApiCallBack<List<InjectRoomBean>>(observable) {
            @Override
            public void onSuccess(List<InjectRoomBean> roomBeanList) {
                callBack.onFinish();
                if (roomBeanList != null && roomBeanList.size() > 0) {
                    callBack.onSuccess(roomBeanList);
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
    public Subscription getInjectEquipmentList(long roomId, @NonNull final IListCallBack<InjectEquipment> callBack) {
        Observable<Bean<List<InjectEquipment>>> observable = Api.createRetrofit().create(Api.InjectEquipmentApi.class).getInjectEquipment(roomId);
        return new ApiCallBack<List<InjectEquipment>>(observable) {
            @Override
            public void onSuccess(List<InjectEquipment> injectEquipments) {
                callBack.onFinish();
                long sysTime = System.currentTimeMillis();
                if (injectEquipments != null && injectEquipments.size() > 0) {
                    for (int i = 0; i < injectEquipments.size(); i++) {
                        if (injectEquipments.get(i).getBeforeInjectionOil() == null && injectEquipments.get(i).getBackInjectionOil() == null) {
                            injectEquipments.get(i).setInjectionOil(null);
                        } else if (injectEquipments.get(i).getBackInjectionOil() == null && injectEquipments.get(i).getBeforeInjectionOil() != null) {
                            injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBeforeInjectionOil());
                        } else if (injectEquipments.get(i).getBackInjectionOil() != null && injectEquipments.get(i).getBeforeInjectionOil() == null) {
                            injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBackInjectionOil());
                        } else {
                            if (injectEquipments.get(i).getBeforeInjectionOil().getNextTime() < injectEquipments.get(i).getBackInjectionOil().getNextTime()) {
                                injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBeforeInjectionOil());
                            } else {
                                injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBackInjectionOil());
                            }
                        }
                        if (injectEquipments.get(i).getInjectionOil() != null) {
                            injectEquipments.get(i).setTime(sysTime - injectEquipments.get(i).getCycle() * 24L * 60L * 60L * 1000L
                                    - injectEquipments.get(i).getInjectionOil().getCreateTime());
                        } else {
                            injectEquipments.get(i).setTime(sysTime);
                        }
                    }
                    Collections.sort(injectEquipments);
                    callBack.onSuccess(injectEquipments);
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
    public Subscription getInjectEquipmentList(Map<String, String> map, @NonNull final IListCallBack<InjectEquipment> callBack) {
        Observable<Bean<List<InjectEquipment>>> observable = Api.createRetrofit().create(InjectOilApi.class).getInjectionEquipmentList(map);
        return new ApiCallBack<List<InjectEquipment>>(observable) {
            @Override
            public void onSuccess(@Nullable List<InjectEquipment> injectEquipments) {
                callBack.onFinish();
                if (injectEquipments != null && injectEquipments.size() > 0) {
                    long sysTime = System.currentTimeMillis();
                    for (int i = 0; i < injectEquipments.size(); i++) {
                        if (injectEquipments.get(i).getBeforeInjectionOil() == null && injectEquipments.get(i).getBackInjectionOil() == null) {
                            injectEquipments.get(i).setInjectionOil(null);
                        } else if (injectEquipments.get(i).getBackInjectionOil() == null && injectEquipments.get(i).getBeforeInjectionOil() != null) {
                            injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBeforeInjectionOil());
                        } else if (injectEquipments.get(i).getBackInjectionOil() != null && injectEquipments.get(i).getBeforeInjectionOil() == null) {
                            injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBackInjectionOil());
                        } else {
                            if (injectEquipments.get(i).getBeforeInjectionOil().getNextTime() < injectEquipments.get(i).getBackInjectionOil().getNextTime()) {
                                injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBeforeInjectionOil());
                            } else {
                                injectEquipments.get(i).setInjectionOil(injectEquipments.get(i).getBackInjectionOil());
                            }
                        }
                        if (injectEquipments.get(i).getInjectionOil() != null) {
                            injectEquipments.get(i).setTime(sysTime - injectEquipments.get(i).getInjectionOil().getNextTime());
                        } else {
                            injectEquipments.get(i).setTime(sysTime);
                        }
                    }
                    Collections.sort(injectEquipments);
                    callBack.onSuccess(injectEquipments);
                } else {
                    callBack.onError("没有数据");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }

    private InjectItemBean mInjectItemBean;

    @NonNull
    @Override
    public Subscription getInjectItem(@NonNull final IObjectCallBack<InjectItemBean> callBack) {
        if (mInjectItemBean != null) {
            for (int i = 0; i < mInjectItemBean.getList().size(); i++) {
                mInjectItemBean.getList().get(i).setValue(null);
            }
            return Observable.just(mInjectItemBean).subscribe(new Subscriber<InjectItemBean>() {

                @Override
                public void onCompleted() {
                    callBack.onFinish();
                }

                @Override
                public void onError(Throwable e) {
                    callBack.onError(e.getMessage());
                }

                @Override
                public void onNext(InjectItemBean injectItemBean) {
                    callBack.onSuccess(injectItemBean);
                }
            });
        }
        Observable<Bean<InjectItemBean>> observable = Api.createRetrofit().create(InjectOilApi.class).getInjectItem();
        return new ApiCallBack<InjectItemBean>(observable) {
            @Override
            public void onSuccess(@Nullable InjectItemBean injectItemBean) {
                callBack.onFinish();
                if (injectItemBean != null && injectItemBean.getList() != null && injectItemBean.getList().size() > 0) {
                    mInjectItemBean = injectItemBean;
                    callBack.onSuccess(injectItemBean);
                } else {
                    callBack.onError("没有数据");
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
    public Subscription injectOilEquipment(JSONObject jsonObject, final IObjectCallBack<String> callBack) {
        Observable<Bean<String>> observable = Api.createRetrofit().create(InjectOilApi.class)
                .injectOil(jsonObject.toString());
        return new ApiCallBack<String>(observable) {
            @Override
            public void onSuccess(@Nullable String o) {
                callBack.onFinish();
                callBack.onSuccess(o);
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
    public Subscription getInjectEquipLogList(Map<String, String> map, @NonNull final IListCallBack<InjectEquipmentLog.ItemList> callBack) {
        Observable<Bean<InjectEquipmentLog>> observable = Api.createRetrofit().create(InjectOilApi.class).getInjectEquipLogs(map);
        return new ApiCallBack<InjectEquipmentLog>(observable) {
            @Override
            public void onSuccess(@Nullable InjectEquipmentLog logs) {
                callBack.onFinish();
                if (logs != null && logs.getList() != null && logs.getList().size() > 0) {
                    callBack.onSuccess(logs.getList());
                } else {
                    callBack.onError("没有数据");
                }
            }

            @Override
            public void onFail() {
                callBack.onFinish();
                callBack.onError("");
            }
        }.execute1();
    }
}
