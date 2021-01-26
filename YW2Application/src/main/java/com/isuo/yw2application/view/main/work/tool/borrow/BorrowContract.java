package com.isuo.yw2application.view.main.work.tool.borrow;

import com.isuo.yw2application.mode.tools.bean.CheckListBean;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 外借
 * Created by zhangan on 2018/4/8.
 */

interface BorrowContract {

    interface Presenter extends BasePresenter {

        void borrowTools(JSONObject jsonObject);

        void getCheckList(Long toolsId);

        void getToolsState(Long toolsId);

        void uploadImage(File image);
    }

    interface View extends BaseView<Presenter> {

        void borrowSuccess();

        void showCheckList(List<CheckListBean> checkListBeans);

        void noCheckList();

        void toolsCanBorrow();

        void toolsCantBorrow();

        void uploadImageSuccess(String url);

        void uploadImageFail();

    }
}
