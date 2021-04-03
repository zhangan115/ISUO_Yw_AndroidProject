package com.isuo.yw2application.view.main.work.tool.detail;

import com.isuo.yw2application.mode.tools.bean.ToolLogListBean;
import com.isuo.yw2application.mode.tools.bean.Tools;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.util.List;

/**
 * 工具详情
 * Created by zhangan on 2018/4/8.
 */

interface ToolsDetailContract {

    interface Presenter extends BasePresenter {

        void askReturn(Long logId);

        void getToolBrowList(Long logId);

        void getToolBrowListMore(Long logId,Long lastId);
    }

    interface View extends BaseView<Presenter> {

        void showTools(Tools tools);

        void noToolBrowLog();

        void noMoreData();

        void refreshFinish();

        void showToolBrowList(List<ToolLogListBean.BrowLogBean> list);

        void showToolBrowListMore(List<ToolLogListBean.BrowLogBean> list);

        void getToolsError();

        void askSuccess();

        void askError();
    }
}
