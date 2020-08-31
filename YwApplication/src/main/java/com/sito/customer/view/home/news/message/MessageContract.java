package com.sito.customer.view.home.news.message;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 消息
 * Created by Administrator on 2017/6/28.
 */
interface MessageContract {

    interface Presenter extends BasePresenter {

        void getUnReadCount();

    }

    interface View extends BaseView<Presenter> {

        void showUnReadCount(int[] unReadCount);
    }

}