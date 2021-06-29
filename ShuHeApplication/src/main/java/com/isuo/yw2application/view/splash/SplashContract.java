package com.isuo.yw2application.view.splash;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by zhangan on 2017-07-24.
 */

public interface SplashContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        //需要登陆
        void needLogin();

        //显示欢迎页
        void showWelcome();

        //进入首页
        void openHome();
    }
}
