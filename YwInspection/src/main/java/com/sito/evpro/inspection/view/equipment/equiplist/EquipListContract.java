package com.sito.evpro.inspection.view.equipment.equiplist;

import com.sito.evpro.inspection.mode.bean.equip.EquipBean;
import com.sito.evpro.inspection.mode.bean.inspection.EquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by Yangzb on 2017/7/18 15:49
 * E-mail：yangzongbin@si-top.com
 */
interface EquipListContract {

    interface Presenter extends BasePresenter {

        void getEquipList();

        void getEquipList(Map<String, Object> map);

        void getMoreEquipList(Map<String, Object> map);

        void getEquipByTaskId(long taskId);
    }

    interface View extends BaseView<Presenter> {
        void showEquip(List<EquipmentBean> list);

        void showMoreEquipment(List<EquipmentBean> list);

        void showData(List<EquipBean> list);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }

}