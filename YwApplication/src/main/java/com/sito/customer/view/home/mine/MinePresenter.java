package com.sito.customer.view.home.mine;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.bean.NewVersion;
import com.sito.customer.mode.customer.CustomerDataSource;
import com.sito.customer.mode.customer.CustomerRepository;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/6/28.
 */
public class MinePresenter implements MineContract.Presenter {

    private CustomerRepository mRepository;
    private MineContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public MinePresenter(CustomerRepository repository, MineContract.View view) {
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
                mView.uploadUserPhotoSuccess();
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
