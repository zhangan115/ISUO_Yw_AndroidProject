package com.isuo.yw2application.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.isuo.yw2application.app.Yw2Application;
import com.isuo.yw2application.mode.Bean;
import com.isuo.yw2application.mode.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 请求回调
 * Created by zhangan on 2017-05-16.
 */

public abstract class ApiCallBack<T> {

    private int mTryCount;
    private Observable<Bean<T>> mObservable;

    public ApiCallBack() {
    }

    public ApiCallBack(@NonNull Observable<Bean<T>> observable) {
        this.mObservable = observable;
    }

    public Subscription execute1() {
        mTryCount = 1;
        return mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable t) {
                        t.printStackTrace();
                        Yw2Application.getInstance().showToast(t.getMessage());
                        onFail();
                        netError();
                    }
                })
                .doOnNext(new Action1<Bean<T>>() {
                    @Override
                    public void call(Bean<T> t) {
                        if (t.getErrorCode() == ApiErrorCode.SUCCEED) {
                            onSuccess(t.getData());
                        } else if (t.getErrorCode() == ApiErrorCode.NOT_LOGGED && mTryCount == 0) {
                            Yw2Application.getInstance().needLogin();
                        } else if (t.getErrorCode() != ApiErrorCode.NOT_LOGGED) {
                            if (!TextUtils.isEmpty(t.getMessage())) {
                                onFail();
                                Yw2Application.getInstance().showToast(t.getMessage());
                            }
                        }
                    }
                })
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
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (mTryCount > 0 && throwable != null && throwable instanceof NotLoggedThrowable) {
                                    --mTryCount;
                                    String name = Yw2Application.getInstance().getUserName();
                                    String pass = Yw2Application.getInstance().getUserPass();
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("userName", name);
                                        jsonObject.put("userPwd", pass);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return Api.createRetrofit().create(Api.Login.class)
                                            .userLogin(jsonObject.toString())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .flatMap(new Func1<Bean<User>, Observable<NotLoggedThrowable>>() {
                                                @Override
                                                public Observable<NotLoggedThrowable> call(Bean<User> userBean) {
                                                    if (userBean.getErrorCode() == 0) {
                                                        Yw2Application.getInstance().setCurrentUser(userBean.getData());
                                                    } else {
                                                        Yw2Application.getInstance().needLogin();
                                                    }
                                                    return Observable.just(new NotLoggedThrowable());
                                                }
                                            }, new Func1<Throwable, Observable<? extends NotLoggedThrowable>>() {
                                                @Override
                                                public Observable<? extends NotLoggedThrowable> call(Throwable throwable) {
                                                    throwable.printStackTrace();
                                                    return Observable.just(new NotLoggedThrowable());
                                                }
                                            }, new Func0<Observable<? extends NotLoggedThrowable>>() {
                                                @Override
                                                public Observable<? extends NotLoggedThrowable> call() {
                                                    return Observable.empty();
                                                }
                                            });
                                } else {
                                    return Observable.empty();
                                }
                            }
                        });
                    }
                }).subscribe(new Subscriber<NotLoggedThrowable>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NotLoggedThrowable notLoggedThrowable) {

                    }
                });
    }


    public abstract void onSuccess(@Nullable T t);

    public abstract void onFail();

    public void netError(){

    };
}
