package com.isuo.yw2application.view.main;

import android.support.annotation.NonNull;

import com.isuo.yw2application.mode.bean.NewVersion;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.io.File;

/**
 * Created by Administrator on 2017/6/28.
 */
interface MainContract {

    interface Presenter extends BasePresenter {

        void getNewVersion();

        void uploadUserPhoto(File file);

        void exitApp();

        void postCidInfo(@NonNull String userCid);

    }

    interface View extends BaseView<Presenter> {

        void newVersionDialog(@NonNull NewVersion version);

        void currentVersion();

        void downLoadApp();

        void uploadUserPhotoSuccess(String url);

        void uploadUserPhotoFail();
    }

}