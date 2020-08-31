package com.sito.customer.view.home;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.NewVersion;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

/**
 * 首页
 * Created by zhangan on 2017-07-25.
 */

public interface HomeContract {

    interface Presenter extends BasePresenter {

        void getNewVersion();

        void postCidInfo(@NonNull String userCid);

        void getUnReadCount();
    }

    interface View extends BaseView<Presenter> {

        void showNewVersion(NewVersion version);

        void showUnReadCount(long count);
    }
}
