package com.sito.customer.view.home.discover.faulttime;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 15:34
 * E-mail：yangzongbin@si-top.com
 */
@Module
class FaultTimeModule {


    private final FaultTimeContract.View mView;

    FaultTimeModule(FaultTimeContract.View view) {
        mView = view;
    }

    @Provides
    FaultTimeContract.View provideFaultTimeContractView() {
        return mView;
    }

}