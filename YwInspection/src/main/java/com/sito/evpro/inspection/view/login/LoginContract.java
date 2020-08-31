package com.sito.evpro.inspection.view.login;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sito.evpro.inspection.mode.bean.User;
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

        void loadHistoryUser(@NonNull String userName, @Nullable List<User> userList);
    }

    interface View extends BaseView<Presenter> {

        void loginSuccess();

        void loginFail();

        void loginAutoFinish();

        void loginNeed();

        void loginLoading();

        void loginHideLoading();

        void showHistoryUser(@NonNull List<User> userList);
    }
}
