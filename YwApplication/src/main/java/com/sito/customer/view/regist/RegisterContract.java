package com.sito.customer.view.regist;

import android.support.annotation.NonNull;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

interface RegisterContract {

    interface Presenter extends BasePresenter {

        void getVerificationCode(@NonNull String phoneNum);

        void toRegister(@NonNull JSONObject json);
    }

    interface View extends BaseView<Presenter> {

        void getSuccess();

        void getFail();

        void registerSuccess();

        void registerFail();

        void showLoading();

        void hideLoading();
    }
}
