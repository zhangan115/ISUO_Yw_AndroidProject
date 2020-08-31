package com.sito.evpro.inspection.view.fault;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/25.
 */
@Module
class FaultModule {

    private final FaultContract.View mView;

    FaultModule(FaultContract.View view) {
        mView = view;
    }

    @Provides
    FaultContract.View provideFaultContractView() {
        return mView;
    }

}