package com.sito.customer.view.home.discover;

import com.sito.customer.mode.bean.discover.StandBean;
import com.sito.customer.mode.bean.discover.ValueAddedBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 发现
 * Created by Administrator on 2017/6/28.
 */
interface DiscoverContract {

    interface Presenter extends BasePresenter {

        void getStandInfo();

        void getValueAdded();
    }

    interface View extends BaseView<Presenter> {

        void showData(List<StandBean.ListBean> ListBean);

        void showValueAddedData(List<ValueAddedBean.Data> ListBean);

        void noData();

        void noValueAdded();
    }

}