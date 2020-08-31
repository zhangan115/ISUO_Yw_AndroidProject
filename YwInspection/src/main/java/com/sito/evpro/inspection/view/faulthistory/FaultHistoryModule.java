package com.sito.evpro.inspection.view.faulthistory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/25.
 */
@Module
class FaultHistoryModule {

    private final FaultHistoryContract.View mView;

    FaultHistoryModule(FaultHistoryContract.View view) {
        mView = view;
    }

    @Provides
    FaultHistoryContract.View provideFaultHistoryContractView() {
        return mView;
    }

}