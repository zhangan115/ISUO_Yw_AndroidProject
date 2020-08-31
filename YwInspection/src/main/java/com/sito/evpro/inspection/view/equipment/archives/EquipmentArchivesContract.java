package com.sito.evpro.inspection.view.equipment.archives;

import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

interface EquipmentArchivesContract {

    interface Presenter extends BasePresenter {

        void getEquipmentDetail(long equipmentId);
    }

    interface View extends BaseView<Presenter> {

        void showEquipmentDetail(EquipmentBean bean);
    }

}
