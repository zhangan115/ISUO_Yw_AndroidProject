package com.sito.customer.view.home.mine;

import android.support.annotation.NonNull;

import com.sito.customer.mode.bean.NewVersion;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.io.File;

/**
 * Created by Administrator on 2017/6/28.
 */
interface MineContract {

    interface Presenter extends BasePresenter {
        void getNewVersion();

        void uploadUserPhoto(File file);

        void exitApp();
    }

    interface View extends BaseView<Presenter> {

        void newVersionDialog(@NonNull NewVersion version);

        void currentVersion();

        void downLoadApp();

        void showUploadPhotoLoading();

        void hideUploadPhotoLoading();

        void uploadUserPhotoSuccess();

        void uploadUserPhotoFail();
    }

}