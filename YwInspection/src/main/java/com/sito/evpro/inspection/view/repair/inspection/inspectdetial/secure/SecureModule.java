package com.sito.evpro.inspection.view.repair.inspection.inspectdetial.secure;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/23 16:52
 * E-mail：yangzongbin@si-top.com
 */
@Module
class SecureModule {


    private final SecureContract.View mView;

    SecureModule(SecureContract.View view) {
        mView = view;
    }

    @Provides
    SecureContract.View provideSecureContractView() {
        return mView;
    }

}