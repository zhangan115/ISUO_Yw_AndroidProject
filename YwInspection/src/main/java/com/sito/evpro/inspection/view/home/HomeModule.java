package com.sito.evpro.inspection.view.home;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/5 18:06
 * E-mail：yangzongbin@si-top.com
 */
@Module
class HomeModule {


    private final HomeContract.View mView;

    HomeModule(HomeContract.View view) {
        mView = view;
    }

    @Provides
    HomeContract.View provideHomeContractView() {
        return mView;
    }

}