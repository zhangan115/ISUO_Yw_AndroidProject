package com.isuo.yw2application.view.main.work.message.join;

import com.isuo.yw2application.mode.bean.JoinBean;
import com.isuo.yw2application.mode.bean.User;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

public interface JoinEnterpriseContract {

    interface Presenter extends BasePresenter {
        void getPlayJoinList();
        void agreeJoin(long id);
    }

    interface View extends BaseView<Presenter> {

        void showPlayJoinList(List<User> list);

        void noData();

        void agreeSuccess();
    }
}
