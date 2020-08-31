package com.sito.evpro.inspection.view.regist;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.Register;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;


/**
 * Created by Administrator on 2017/6/2.
 */
interface RegisterContract {

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