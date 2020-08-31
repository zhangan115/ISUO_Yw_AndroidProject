package com.sito.evpro.inspection.view.setting.forgepassword;

import android.support.annotation.NonNull;


import com.sito.evpro.inspection.mode.IObjectCallBack;
import com.sito.evpro.inspection.mode.inspection.InspectionRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-26.
 */

class ForgePassWordPresenter implements ForgePassWordContract.Presenter {

    private InspectionRepository mRepository;
    private ForgePassWordContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    ForgePassWordPresenter(InspectionRepository repository, ForgePassWordContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void updatePassWord(String oldPassWord, String newPassWord) {
        mSubscription.add(mRepository.updateUserPassWord(oldPassWord, newPassWord, new IObjectCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                mView.updateSuccess();
            }

            @Override
            public void onError(String message) {
                mView.updateFail();
            }

            @Override
            public void onFinish() {

            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
