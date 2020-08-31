package com.sito.customer.view.login;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.customer.mode.bean.User;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 用户登录
 * Created by zhangan on 2017-02-16.
 */
interface LoginContract {

    interface Presenter extends BasePresenter {

        void login(String name, String pass);

    }

    interface View extends BaseView<Presenter> {

        void loginSuccess();

        void loginFail();

        void loginLoading();

        void loginHideLoading();

    }
}
