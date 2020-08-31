package com.sito.evpro.inspection.view.setting.forgepassword;

import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * Created by zhangan on 2017-07-26.
 */

public interface ForgePassWordContract {
    interface Presenter extends BasePresenter {

        void updatePassWord(String oldPassWord, String newPassWord);
    }

    interface View extends BaseView<Presenter> {

        void updateSuccess();

        void updateFail();
    }
}
