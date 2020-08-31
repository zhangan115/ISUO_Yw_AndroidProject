package com.sito.customer.view.home.work.tool.borrow;

import com.sito.customer.mode.tools.bean.CheckListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

interface BorrowContract {

    interface Presenter extends BasePresenter {

        void borrowTools(JSONObject jsonObject);

        void getCheckList(Long toolsId);

        void getToolsState(Long toolsId);
    }

    interface View extends BaseView<Presenter> {

        void borrowSuccess();

        void showCheckList(List<CheckListBean> checkListBeans);

        void noCheckList();

        void toolsCanBorrow();

        void toolsCantBorrow();

    }
}
