package com.sito.evpro.inspection.utils;

import android.content.Context;

import com.sito.library.luban.Luban;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pingan on 2017/7/11.
 */

public class PhotoUtils {


    public interface PhotoListener {
        void onSuccess(File file);
    }


    public static Subscription cropPhoto(final Context context, final File photoFile, final PhotoListener listener) {
        return Observable.just(photoFile)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        File luBanFile = null;
                        try {
                            luBanFile = Luban.with(context).load(file).get().get(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (luBanFile != null && file.exists()) {
                            file.delete();
                        }
                        return Observable.just(luBanFile);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(File file) {
                        if (listener == null || file == null) {
                            return;
                        }
                        listener.onSuccess(file);
                    }
                });

    }
}