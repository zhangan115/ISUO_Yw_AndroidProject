package com.isuo.yw2application.view.main.work.pay;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

public interface PayContract {

    interface Presenter extends BasePresenter {

        void getPayList(JSONObject jsonObject);

        void pay(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void showPayList();


        void paySuccess();

        void payFail();
    }
}
