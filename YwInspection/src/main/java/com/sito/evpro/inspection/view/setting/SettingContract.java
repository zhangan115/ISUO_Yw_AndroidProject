package com.sito.evpro.inspection.view.setting;

import android.support.annotation.NonNull;

import com.sito.evpro.inspection.mode.bean.NewVersion;
import com.sito.library.base.BasePresenter;
import com.sito.library.base.BaseView;

import java.io.File;

/**
 * Created by Yangzb on 2017/7/4 13:32
 * E-mail：yangzongbin@si-top.com
 */
interface SettingContract {

    interface Presenter extends BasePresenter {
        void getNewVersion();

        void uploadUserPhoto(File file);
    }

    interface View extends BaseView<Presenter> {

        void newVersionDialog(@NonNull NewVersion version);

        void currentVersion();

        void downLoadApp();

        void uploadUserPhotoSuccess();

        void uploadUserPhotoFail();
    }

}