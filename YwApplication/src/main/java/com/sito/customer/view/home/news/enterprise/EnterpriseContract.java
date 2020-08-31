package com.sito.customer.view.home.news.enterprise;

import com.sito.customer.mode.bean.news.EnterpriseDetailBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 通知详情
 * Created by zhangan on 2018/4/23.
 */

interface EnterpriseContract {

    interface Presenter extends BasePresenter {

        void getMessageDetail(long messageId);

        void downLoadFile(String url, String savePath, String fileName);
    }

    interface View extends BaseView<Presenter> {

        void showEnterpriseDetailBean(EnterpriseDetailBean bean);

        void noData();

        void downLoadSuccess();

        void showDownLoading();

        void hideDownLoading();
    }
}
