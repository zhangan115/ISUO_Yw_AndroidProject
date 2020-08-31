package com.sito.customer.view.home.work.tool.add;

import com.sito.customer.mode.tools.bean.Tools;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.io.File;

/**
 * 增加/修改工具
 * Created by zhangan on 2018/4/4.
 */

public interface AddToolsContract {

    interface Presenter extends BasePresenter {

        void uploadImage(File image);

        void addTools(Tools tools);
    }

    interface View extends BaseView<Presenter> {

        void uploadToolsSuccess();

        void uploadToolsFail();

        void uploadImageSuccess(String url);

        void uploadImageFail();

    }
}
