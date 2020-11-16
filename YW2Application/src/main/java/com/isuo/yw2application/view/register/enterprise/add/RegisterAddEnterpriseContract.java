package com.isuo.yw2application.view.register.enterprise.add;

import com.isuo.yw2application.mode.bean.User;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

public interface RegisterAddEnterpriseContract {

    interface Presenter extends BasePresenter {

        void getEnterpriseCustomerList(JSONObject json);

        void askCustomer(JSONObject jsonObject);

    }

    interface View extends BaseView<Presenter> {

        void showData(List<User.CustomerBean> list);

        void joinAskSuccess();

        void joinAskError();

        void showLoading();

        void noData();
    }


}
