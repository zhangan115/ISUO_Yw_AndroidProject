package com.sito.customer.view.home.work.inject.oil;

import com.sito.customer.mode.inject.bean.InjectItemBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.Map;

/**
 * 注油
 * Created by zhangan on 2018/4/12.
 */

interface InjectOilContract {

    interface Presenter extends BasePresenter {

        void getInjectList();

        void injectOilEquipment(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void showInjectItem(InjectItemBean itemBean);

        void noData();

        void injectSuccess();

        void injectFail();
    }
}
