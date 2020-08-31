package com.sito.evpro.inspection.view.setting;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 13:35
 * E-mail：yangzongbin@si-top.com
 */
@Module
class SettingModule {


    private final SettingContract.View mView;

    SettingModule(SettingContract.View view) {
        mView = view;
    }

    @Provides
    SettingContract.View provideSettingContractView() {
        return mView;
    }

}