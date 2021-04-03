package com.isuo.yw2application.view.pass;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/6/2.
 */
@Module
class ForgePassModule {

    private final ForgePassContract.View mView;

    ForgePassModule(ForgePassContract.View view) {
        mView = view;
    }

    @Provides
    ForgePassContract.View provideRegisterContractView() {
        return mView;
    }

}