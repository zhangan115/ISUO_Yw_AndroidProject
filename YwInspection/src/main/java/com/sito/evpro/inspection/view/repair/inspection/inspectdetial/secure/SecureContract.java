package com.sito.evpro.inspection.view.repair.inspection.inspectdetial.secure;

import com.sito.evpro.inspection.mode.bean.inspection.SecureBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by Yangzb on 2017/7/23 16:50
 * E-mail：yangzongbin@si-top.com
 */
interface SecureContract {

    interface Presenter extends BasePresenter {
        void getSecureInfo(long securityId);
    }

    interface View extends BaseView<Presenter> {
        void showData(SecureBean secureBean);

        void showLoading();

        void hideLoading();

        void noData();
    }

}