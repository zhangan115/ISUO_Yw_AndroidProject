package com.isuo.yw2application.view.main.work.safe;

import com.isuo.yw2application.mode.bean.news.MessageListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 火警，救护详情
 * Created by zhangan on 2018/4/23.
 */

interface NewsSafeContract {

    interface Presenter extends BasePresenter {

        void getMessageDetail(long messageId);
    }

    interface View extends BaseView<Presenter> {

        void showData(MessageListBean bean);

        void noData();
    }
}
