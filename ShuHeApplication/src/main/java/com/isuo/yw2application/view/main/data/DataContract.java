package com.isuo.yw2application.view.main.data;

import com.isuo.yw2application.mode.bean.discover.ValueAddedBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 发现
 * Created by Administrator on 2017/6/28.
 */
interface DataContract {

    interface Presenter extends BasePresenter {

        void getValueAdded();
    }

    interface View extends BaseView<Presenter> {

        void showValueAddedData(List<ValueAddedBean.Data> ListBean);

        void noData();

        void noValueAdded();
    }

}