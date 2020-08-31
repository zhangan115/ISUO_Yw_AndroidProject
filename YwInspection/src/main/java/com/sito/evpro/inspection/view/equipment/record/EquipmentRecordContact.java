package com.sito.evpro.inspection.view.equipment.record;

import com.sito.evpro.inspection.mode.bean.overhaul.OverhaulBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by pingan on 2017/7/3.
 */

 interface EquipmentRecordContact {
    interface Presenter extends BasePresenter {
        void getOverByEId(long equipId);

        void getMoreOverByEId(long equipId, int lastId);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<OverhaulBean> list);

        void showMoreData(List<OverhaulBean> list);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }
}
