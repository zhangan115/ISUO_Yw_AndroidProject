package com.sito.evpro.inspection.view.increment.history;

import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/6 18:01
 * E-mail：yangzongbin@si-top.com
 */
interface IncreHistoryContract {

    interface Presenter extends BasePresenter {
        void getIncreList(int count);

        void getMoreIncreList(int count, long lastId);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<IncrementBean> lists);

        void showMoreData(List<IncrementBean> lists);

        void noMoreData();

        void hideLoadingMore();

        void showLoading();

        void hideLoading();

        void noData();
    }

}