package com.isuo.yw2application.view.main.work.safe_manager;

import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

public interface SafeManagerContract {

    interface Presenter extends BasePresenter {

        void getSafeManagerList();

    }

    interface View extends BaseView<Presenter> {
        void showData(List<StandBean.ListBean> ListBean);
        void noData();
    }
}
