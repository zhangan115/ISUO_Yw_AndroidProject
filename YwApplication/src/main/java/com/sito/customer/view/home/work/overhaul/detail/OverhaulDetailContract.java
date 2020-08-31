package com.sito.customer.view.home.work.overhaul.detail;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.overhaul.OverhaulBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 检修详情
 * Created by zhangan on 2017-07-17.
 */

interface OverhaulDetailContract {

    interface Presenter extends BasePresenter {

        void getRepairDetailData(@NonNull String repairId);
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(@NonNull OverhaulBean repairWorkBean);

        void noData();
    }
}
