package com.sito.customer.view.home.work.increment.detail;

import com.sito.customer.mode.bean.work.IncrementBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by zhangan on 2017/10/17.
 */

public interface IncrementDetailContract {

    interface Presenter extends BasePresenter {

        void getIncrementData(long workId);
    }

    interface View extends BaseView<Presenter> {

        void showIncrement(IncrementBean incrementBean);

        void showLoading();

        void hideLoading();

        void noData();
    }
}
