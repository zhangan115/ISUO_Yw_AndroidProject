package com.isuo.yw2application.view.main;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.IObjectCallBack;
import com.isuo.yw2application.mode.bean.NewVersion;
import com.isuo.yw2application.mode.customer.CustomerDataSource;
import com.isuo.yw2application.mode.customer.CustomerRepository;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/28.
 */
public class MainPresenter implements MainContract.Presenter {

    private CustomerRepository mRepository;
    private MainContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public MainPresenter(CustomerRepository repository, MainContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    @Override
    public void getNewVersion() {
        mSubscription.add(mRepository.getNewVersion(new CustomerDataSource.NewVersionCallBack() {
            @Override
            public void newVersion(NewVersion result) {
                mView.newVersionDialog(result);
            }

            @Override
            public void noNewVersion() {
                mView.currentVersion();
            }
        }));
    }

    @Override
    public void uploadUserPhoto(File file) {
        mSubscription.add(mRepository.uploadUserPhoto(file, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.uploadUserPhotoSuccess(s);
            }

            @Override
            public void onError(String message) {
                mView.uploadUserPhotoFail();
            }

            @Override
            public void onFinish() {
            }
        }));
    }

    @Override
    public void exitApp() {
        mSubscription.add(mRepository.exitApp());
    }
}
