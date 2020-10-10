package com.isuo.yw2application.view.main.work.tool.return_tools;

import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.isuo.yw2application.mode.tools.bean.ToolsLog;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

interface ReturnToolsContract {

    interface Presenter extends BasePresenter {

        void getToolsLog(Long toolsId);

        void returnTools(JSONObject jsonObject);

    }

    interface View extends BaseView<Presenter> {

        void returnSuccess();

        void showToolsLog(ToolsLog toolsLog);

        void toolsLogError();

        void showCheckList(List<CheckListBean> checkListBeans);

        void noCheckList();
    }
}
