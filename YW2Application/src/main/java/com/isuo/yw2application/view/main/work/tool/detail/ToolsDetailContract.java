package com.isuo.yw2application.view.main.work.tool.detail;

import com.isuo.yw2application.mode.tools.bean.Tools;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 工具详情
 * Created by zhangan on 2018/4/8.
 */

interface ToolsDetailContract {

    interface Presenter extends BasePresenter {

        void askReturn(Long logId);
    }

    interface View extends BaseView<Presenter> {

        void showTools(Tools tools);

        void getToolsError();

        void askSuccess();

        void askError();
    }
}
