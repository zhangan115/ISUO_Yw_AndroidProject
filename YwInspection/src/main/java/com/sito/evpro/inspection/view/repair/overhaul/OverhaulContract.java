package com.sito.evpro.inspection.view.repair.overhaul;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by zhangan on 2017-06-22.
 */
interface OverhaulContract {

    interface Presenter extends BasePresenter {

        void getOverhaulList(@NonNull String time);

        void getOverhaulListMore(@NonNull String time, @NonNull String lastId);

        void startOverhaul(int position, long overhaulId);
    }

    interface View extends BaseView<Presenter> {

        void showData(List<OverhaulBean> overhaulBeen);

        void showMoreData(List<OverhaulBean> overhaulBeen);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();

        void startSuccess(int position);

        void startFail();

    }

}
