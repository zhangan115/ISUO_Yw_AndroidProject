package com.isuo.yw2application.view.main.task.overhaul;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.overhaul.OverhaulBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 检修
 * Created by zhangan on 2017-06-22.
 */
interface OverhaulContract {

    interface Presenter extends BasePresenter {

        void getData(@NonNull String time);

    }

    interface View extends BaseView<Presenter> {

        void showData(List<OverhaulBean> been);

        void showLoading();

        void hideLoading();

        void noData();
    }

}
