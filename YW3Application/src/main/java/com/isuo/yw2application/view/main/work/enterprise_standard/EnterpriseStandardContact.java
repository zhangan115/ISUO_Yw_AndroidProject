package com.isuo.yw2application.view.main.work.enterprise_standard;

import com.isuo.yw2application.mode.bean.discover.StandBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

public interface EnterpriseStandardContact {

    interface Presenter extends BasePresenter {

        void getEnterpriseStandardList();

    }

    interface View extends BaseView<Presenter> {

        void showData(List<StandBean.ListBean> ListBean);

        void noData();
    }
}
