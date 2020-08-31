package com.sito.evpro.inspection.api;


import com.sito.evpro.inspection.mode.Bean;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http 网络请求 manager
 * Created by zhangan on 2017-03-16.
 */
public class HTTPManager<T> {

    private Observable<Bean<T>> observable;

    public HTTPManager(Observable<Bean<T>> observable) {
        this.observable = observable;
    }

    public Subscription subscribe(ApiCallBack1<T> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}