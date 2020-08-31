package com.sito.evpro.inspection.view.repair.increment;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.increment.IncrementBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-22.
 */
interface IncrementContract {

    interface Presenter extends BasePresenter {

        void getIncrementList(@NonNull String time);

        void getIncrementListMore(@NonNull String time, @NonNull String lastId);

        void startIncrement(int position, long workId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<IncrementBean> incrementBeen);

        void showMoreData(List<IncrementBean> incrementBeen);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();

        void startSuccess(int position);

    }

}
