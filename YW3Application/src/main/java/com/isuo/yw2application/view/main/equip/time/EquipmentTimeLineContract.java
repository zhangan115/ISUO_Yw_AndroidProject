package com.isuo.yw2application.view.main.equip.time;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.equip.TimeLineBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 大修记录，带电检测，实验数据
 * Created by zhangan on 2017/10/13.
 */

interface EquipmentTimeLineContract {

    interface Presenter extends BasePresenter {

        void getEquipRepairData(long equipmentId);

        void getEquipCheckData(long equipmentId);

        void getEquipExperimentData(long equipmentId);
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(@NonNull List<TimeLineBean> lineBeen);

        void noData();

        void showMessage(String message);
    }
}
