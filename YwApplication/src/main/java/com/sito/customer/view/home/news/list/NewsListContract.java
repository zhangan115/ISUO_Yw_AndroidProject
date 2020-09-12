package com.sito.customer.view.home.news.list;

import com.sito.customer.mode.bean.news.MessageListBean;
import com.sito.customer.mode.bean.db.NewsBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * 新消息提示
 * Created by zhangan on 2017/10/16.
 */
interface NewsListContract {

    interface Presenter extends BasePresenter {

        void borrowSure(long toolsId, int state);

        void getMessageList(Map<String, String> map);

        void getMessageListMore(Map<String, String> map);
    }

    interface View extends BaseView<Presenter> {

        void showMessage(List<NewsBean> list);

        void noData();

        void showLoading();

        void hideLoading();

        void noMoreData();

        void loadMoreFinish();

        void showMessageList(List<MessageListBean> list);

        void showMessageListMore(List<MessageListBean> list);

        void borrowSureSuccess();

    }
}
