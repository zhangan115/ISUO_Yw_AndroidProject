package com.isuo.yw2application.view.main.task.overhaul.security;

import com.isuo.yw2application.mode.bean.inspection.SecureBean;
import com.isuo.yw2application.mode.bean.overhaul.OverhaulNoteBean;
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

        void showData(SecureBean noteBean);

        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void noData();
    }
}
