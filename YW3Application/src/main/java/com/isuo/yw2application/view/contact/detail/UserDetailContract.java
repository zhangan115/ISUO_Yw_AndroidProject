package com.isuo.yw2application.view.contact.detail;

import com.isuo.yw2application.mode.bean.User;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

public interface UserDetailContract {

    interface Presenter extends BasePresenter {
        void getUserInfo(int userId);

        void saveUserInfo(JSONObject jsonObject);
    }

    interface View extends BaseView<Presenter> {

        void showUserInfo(User user);

        void saveSuccess();
    }
}
