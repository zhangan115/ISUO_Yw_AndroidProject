package com.sito.customer.view.equip.archives;

import com.sito.customer.mode.bean.equip.EquipmentBean;
import com.sito.customer.mode.bean.equip.FocusBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 设备档案
 * Created by zhangan on 2017/10/12.
 */

interface EquipmentArchivesContract {

    interface Presenter extends BasePresenter {

        void getEquipmentDetail(long equipmentId);

        /**
         * 获取设备重点关注数据
         *
         * @param equipmentId 设备id
         */
        void getEquipmentCare(long equipmentId);
    }

    interface View extends BaseView<Presenter> {

        void showEquipmentDetail(EquipmentBean bean);

        /**
         * 显示重点关注信息
         *
         * @param f 关注信息
         */
        void showCareData(FocusBean f);

        /**
         * 请求重点关注信息出错
         */
        void showCareDataFail();

    }

}
