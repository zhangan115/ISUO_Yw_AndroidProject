package com.sito.customer.view.home.work.inspection.security;

import com.sito.customer.mode.bean.inspection.SecureBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 安全包
 * Created by zhangan on 2018/3/20.
 */

interface SecurityPackageContract {

    interface Presenter extends BasePresenter {

        void getSecureInfo(long securityId);

    }

    interface View extends BaseView<Presenter> {

        void showData(SecureBean secureBean);

        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void noData();
    }
}
