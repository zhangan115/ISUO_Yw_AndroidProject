package com.sito.customer.view.equip.data;


import com.sito.customer.mode.bean.check.CheckValue;
import com.sito.customer.mode.bean.check.InspectionData;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by pingan on 2017/7/3.
 */

interface EquipmentDataContact {
    interface Presenter extends BasePresenter {
        void getCheckData(long equipId);

        void getCheckValue(long equipId, int inspecId);
    }

    interface View extends BaseView<Presenter> {
        void showCheckData(InspectionData checkDatas);

        void showCheckValue(CheckValue checkValue);

        void showLoading();

        void hideLoading();

        void noData();
    }
}
