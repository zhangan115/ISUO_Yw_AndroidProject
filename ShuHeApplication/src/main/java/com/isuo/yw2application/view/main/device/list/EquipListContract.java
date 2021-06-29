package com.isuo.yw2application.view.main.device.list;

import com.isuo.yw2application.mode.bean.equip.EquipBean;
import com.isuo.yw2application.mode.bean.equip.EquipmentBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by Yangzb on 2017/7/15 12:29
 * E-mail：yangzongbin@si-top.com
 */
interface EquipListContract {

    interface Presenter extends BasePresenter {

        void getEquipInfo(boolean isFocusNow);

        void getEquipList(Map<String, Object> map);

        void getMoreEquipList(Map<String, Object> map);

        void getEquipByTaskId(long taskId);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<EquipBean> list);

        void showEquip(List<EquipmentBean> list);

        void showMoreEquip(List<EquipmentBean> list);

        void showLoading();

        void hideLoading();

        void noData();

        void noMoreData();

        void hideLoadingMore();
    }

}