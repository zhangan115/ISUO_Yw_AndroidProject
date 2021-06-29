package com.isuo.yw2application.view.main.work.inject.oil;

import com.isuo.yw2application.mode.inject.bean.InjectItemBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

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
