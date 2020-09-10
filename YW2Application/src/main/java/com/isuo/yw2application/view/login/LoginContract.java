package com.isuo.yw2application.view.login;


import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

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
