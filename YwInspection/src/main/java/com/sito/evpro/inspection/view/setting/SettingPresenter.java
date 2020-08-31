package com.sito.evpro.inspection.view.setting;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.evpro.inspection.mode.inspection.InspectionDataSource;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import java.io.File;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Yangzb on 2017/7/4 13:36
 * E-mail：yangzongbin@si-top.com
 */
final class SettingPresenter implements SettingContract.Presenter {
    private InspectionRepository mRepository;
    private SettingContract.View mView;
    @NonNull
    private CompositeSubscription mSubscruptions;

    @Inject
    SettingPresenter(InspectionRepository repository, SettingContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscruptions = new CompositeSubscription();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getNewVersion() {
        mSubscruptions.add(mRepository.getNewVersion(new InspectionDataSource.NewVersionCallBack() {
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
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscruptions.clear();
    }

    @Override
    public void uploadUserPhoto(File file) {
        mSubscruptions.add(mRepository.uploadUserPhoto(file, new IObjectCallBack<String>() {
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
}