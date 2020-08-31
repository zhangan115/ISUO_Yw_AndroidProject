package com.sito.evpro.inspection.view.increment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/25.
 */
@Module
class IncrementModule {

    private final IncrementContract.View mView;

    IncrementModule(IncrementContract.View view) {
        mView = view;
    }

    @Provides
    IncrementContract.View provideIncrementContractView() {
        return mView;
    }

}