package com.sito.customer.view.home.mine.forgepassword;

import android.support.annotation.NonNull;

import com.sito.customer.mode.IObjectCallBack;
import com.sito.customer.mode.customer.CustomerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangan on 2017-07-26.
 */

public class ForgePassWordPresenter implements ForgePassWordContract.Presenter {

    private CustomerRepository mRepository;
    private ForgePassWordContract.View mView;
    @NonNull
    private CompositeSubscription mSubscription;

    public ForgePassWordPresenter(CustomerRepository repository, ForgePassWordContract.View view) {
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
