package com.sito.evpro.inspection.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sito.evpro.inspection.app.InspectionApp;
import com.sito.evpro.inspection.mode.Bean;
import com.sito.evpro.inspection.mode.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 请求回调
 * Created by zhangan on 2017-05-16.
 */

public abstract class ApiCallBack2<T> {

    private int mTryCount;
    private Observable<Bean<T>> mObservable;

    public ApiCallBack2() {
    }

    public ApiCallBack2(@NonNull Observable<Bean<T>> observable) {
        this.mObservable = observable;
    }

    public void setObservable(Observable<Bean<T>> mObservable) {
        this.mObservable = mObservable;
    }

    public Observable<?> execute() {
        mTryCount = 1;
        return mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable t) {
                        t.printStackTrace();
                        InspectionApp.getInstance().showToast(t.getMessage());
                        onFail();
                    }
                })
                .doOnNext(new Action1<Bean<T>>() {
                    @Override
                    public void call(Bean<T> t) {
                        if (t.getErrorCode() == ApiErrorCode.SUCCEED) {
                            onSuccess(t.getData());
                        } else if (t.getErrorCode() == ApiErrorCode.NOT_LOGGED && mTryCount == 0) {
                            InspectionApp.getInstance().needLogin();
                        } else if (t.getErrorCode() != ApiErrorCode.NOT_LOGGED) {
                            if (!TextUtils.isEmpty(t.getMessage())) {
                                onFail();
                                InspectionApp.getInstance().showToast(t.getMessage());
                            }
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Bean<T>, Observable<NotLoggedThrowable>>() {
                    @Override
                    public Observable<NotLoggedThrowable> call(Bean<T> customerBean) {
                        if (mTryCount > 0 && customerBean.getErrorCode() == ApiErrorCode.NOT_LOGGED) {
                            return Observable.error(new NotLoggedThrowable());
                        }
                        return Observable.just(null);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<Bean<User>>>() {
                            @Override
                            public Observable<Bean<User>> call(Throwable throwable) {
                                if (mTryCount > 0 && throwable != null && throwable instanceof NotLoggedThrowable) {
                                    --mTryCount;
                                    String name = InspectionApp.getInstance().getUserName();
                                    String pass = InspectionApp.getInstance().getUserPass();
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("userName", name);
                                        jsonObject.put("userPwd", pass);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return Api.createRetrofit().create(Api.Login.class)
                                            .userLogin(jsonObject.toString())
                                            .doOnNext(new Action1<Bean<User>>() {
                                                @Override
                                                public void call(Bean<User> userBean) {
                                                    if (userBean.getErrorCode() == 0) {
                                                        InspectionApp.getInstance().setCurrentUser(userBean.getData());
                                                    }
                                                }
                                            });
                                } else {
                                    return Observable.empty();
                                }
                            }
                        });
                    }
                })
                .flatMap(new Func1<NotLoggedThrowable, Observable<?>>() {
                    @Override
                    public Observable<?> call(NotLoggedThrowable notLoggedThrowable) {
                        return null;
                    }
                });
    }


    public abstract void onSuccess(T t);

    public abstract void onFail();
}
