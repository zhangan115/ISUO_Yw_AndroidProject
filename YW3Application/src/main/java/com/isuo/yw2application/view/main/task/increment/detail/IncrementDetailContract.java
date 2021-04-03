package com.isuo.yw2application.view.main.task.increment.detail;

import com.isuo.yw2application.mode.bean.work.IncrementBean;
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
