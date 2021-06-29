package com.isuo.yw2application.view.register.enterprise.create;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

public interface RegisterCreateEnterpriseContract {

    interface Presenter extends BasePresenter {

        void createEnterpriseCustomer(JSONObject json);

    }

    interface View extends BaseView<Presenter> {

        void createSuccess();

        void createError();
    }


}
