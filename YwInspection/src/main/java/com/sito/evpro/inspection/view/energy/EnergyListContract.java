package com.sito.evpro.inspection.view.energy;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.energy.EnergyBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by Administrator on 2017/6/22.
 */
interface EnergyListContract {

    interface Presenter extends BasePresenter {
        void getEnergyInfo();
    }

    interface View extends BaseView<Presenter> {
        void showData(@NonNull EnergyBean bean);

        void showLoading();

        void hideLoading();

        void noData();
    }

}