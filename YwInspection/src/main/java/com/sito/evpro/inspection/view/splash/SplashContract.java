package com.sito.evpro.inspection.view.splash;

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

        //进入首页
        void openHome();
    }
}
