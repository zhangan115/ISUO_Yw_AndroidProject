package com.isuo.yw2application.view.main.task.increment;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.work.IncrementBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 专项工作
 * Created by zhangan on 2017-06-22.
 */
interface IncrementContract {

    interface Presenter extends BasePresenter {

        void getData(String time, boolean isFinish);

        void getDataMore(String time, boolean isFinish, @NonNull String lastId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<IncrementBean> been);

        void showMoreData(List<IncrementBean> been);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }

}
