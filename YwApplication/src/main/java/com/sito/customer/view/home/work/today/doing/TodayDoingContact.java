package com.sito.customer.view.home.work.today.doing;

import com.sito.customer.mode.bean.today.TodayToDoBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

interface TodayDoingContact {

    interface Presenter extends BasePresenter {
        void getData();
    }

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showData(List<TodayToDoBean> datas);

        void noData();
    }
}
