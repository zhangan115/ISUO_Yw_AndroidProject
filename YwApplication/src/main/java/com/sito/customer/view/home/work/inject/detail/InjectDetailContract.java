package com.sito.customer.view.home.work.inject.detail;

import com.sito.customer.mode.inject.bean.InjectEquipmentLog;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * 注油详情
 * Created by zhangan on 2018/4/12.
 */

interface InjectDetailContract {

    interface Presenter extends BasePresenter {

        void getInjectDetailList(Map<String, String> map);

        void getInjectDetailListMore(Map<String, String> map);
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showInjectDetailData(List<InjectEquipmentLog.ItemList> list);

        void noData();

        void showMoreInjectDetailData(List<InjectEquipmentLog.ItemList> list);

        void noMoreData();

        void hideLoadingMore();

    }
}
