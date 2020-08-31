package com.sito.evpro.inspection.view.equipment.equiplist.search;

import com.sito.evpro.inspection.mode.bean.equip.EquipRoom;
import com.sito.evpro.inspection.mode.bean.equip.EquipType;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * Created by Yangzb on 2017/7/10 18:15
 * E-mail：yangzongbin@si-top.com
 */
interface EquipSearchContract {

    interface Presenter extends BasePresenter {
        void getEquipType();

        void getEquipPlace();
    }

    interface View extends BaseView<Presenter> {
        void showEquipType(List<EquipType> equipTypes);

        void showEquipRoom(List<EquipRoom> equipRooms);

        void showLoading();

        void hideLoading();

        void noDdata();
    }

}