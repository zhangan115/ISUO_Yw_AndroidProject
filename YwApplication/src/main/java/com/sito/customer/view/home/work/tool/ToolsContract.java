package com.sito.customer.view.home.work.tool;

import com.sito.customer.mode.tools.bean.Tools;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * 工具
 * Created by zhangan on 2018/4/2.
 */

interface ToolsContract {

    interface Presenter extends BasePresenter {

        void searchTools(Map<String, String> map);

        void getToolsList(Map<String, String> map);

        void getToolsListMore(Map<String, String> map);
    }

    interface View extends BaseView<Presenter> {

        void showTools(List<Tools> tools);

        void showMoreTools(List<Tools> tools);

        void noData();

        void showMessage(String message);

        void showLoading();

        void hideLoading();

        void noMoreData();

        void hideLoadingMore();
    }

}
