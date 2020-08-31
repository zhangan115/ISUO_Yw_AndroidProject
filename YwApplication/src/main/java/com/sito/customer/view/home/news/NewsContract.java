package com.sito.customer.view.home.news;

import com.sito.customer.mode.bean.news.Aggregate;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 消息累
 * Created by Administrator on 2017/6/28.
 */
interface NewsContract {

    interface Presenter extends BasePresenter {

        void getUnReadCount();

    }

    interface View extends BaseView<Presenter> {

        void showUnReadCount(int[] unReadCount);
    }

}