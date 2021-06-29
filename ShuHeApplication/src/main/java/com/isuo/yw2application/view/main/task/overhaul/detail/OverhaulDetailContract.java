package com.isuo.yw2application.view.main.task.overhaul.detail;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
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
