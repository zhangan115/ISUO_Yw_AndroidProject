package com.sito.evpro.inspection.view.regist;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/6/2.
 */
@Module
class RegisterModule {

    private final RegisterContract.View mView;

    RegisterModule(RegisterContract.View view) {
        mView = view;
    }

    @Provides
    RegisterContract.View provideRegisterContractView() {
        return mView;
    }

}