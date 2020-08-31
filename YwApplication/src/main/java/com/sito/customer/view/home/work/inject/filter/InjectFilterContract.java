package com.sito.customer.view.home.work.inject.filter;

import com.sito.customer.mode.inject.bean.InjectRoomBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 注油
 * Created by zhangan on 2018/4/10.
 */

public interface InjectFilterContract {

    interface Presenter extends BasePresenter {
        void requestRoomList();

    }

    interface View extends BaseView<Presenter> {

        void showRoomList(List<InjectRoomBean> roomBeans);

        void noData();
    }
}
