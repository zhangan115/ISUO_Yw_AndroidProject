package com.sito.customer.view.pass;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.Register;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;


/**
 * Created by Administrator on 2017/6/2.
 */
interface ForgePassContract {

    interface Presenter extends BasePresenter {

        void getVerificationCode(@NonNull String phoneNum);

        void toRegister(@NonNull Register register);
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