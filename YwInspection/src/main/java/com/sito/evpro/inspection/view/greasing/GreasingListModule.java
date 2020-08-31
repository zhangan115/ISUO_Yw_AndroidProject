package com.sito.evpro.inspection.view.greasing;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/22.
 */
@Module
class GreasingListModule {

    private final GreasingListContract.View mView;

    GreasingListModule(GreasingListContract.View view) {
        mView = view;
    }

    @Provides
    GreasingListContract.View provideGreasingListContractView() {
        return mView;
    }

}