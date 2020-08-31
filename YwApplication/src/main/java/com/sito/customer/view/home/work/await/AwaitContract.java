package com.sito.customer.view.home.work.await;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.work.AwaitWorkBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 代办任务
 * Created by zhangan on 2017-06-29.
 */

public interface AwaitContract {

    interface Presenter extends BasePresenter {

        void getData(String date);

        void getDataMore(String date, @NonNull String lastId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<AwaitWorkBean> awaitWorkBeen);

        void showMoreData(List<AwaitWorkBean> awaitWorkBeen);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }
}
