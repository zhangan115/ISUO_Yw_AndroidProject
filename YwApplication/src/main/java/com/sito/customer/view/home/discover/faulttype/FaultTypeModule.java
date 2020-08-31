package com.sito.customer.view.home.discover.faulttype;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 15:45
 * E-mail：yangzongbin@si-top.com
 */
@Module
class FaultTypeModule {


    private final FaultTypeContract.View mView;

    FaultTypeModule(FaultTypeContract.View view) {
        mView = view;
    }

    @Provides
    FaultTypeContract.View provideFaultTypeContractView() {
        return mView;
    }

}