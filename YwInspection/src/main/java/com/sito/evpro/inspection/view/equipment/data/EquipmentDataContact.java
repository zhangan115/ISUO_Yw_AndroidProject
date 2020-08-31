package com.sito.evpro.inspection.view.equipment.data;


import com.sito.evpro.inspection.mode.bean.equip.CheckValue;
import com.sito.evpro.inspection.mode.bean.equip.InspectionData;
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
