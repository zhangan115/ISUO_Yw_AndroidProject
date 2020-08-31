package com.sito.evpro.inspection.view.increment.history;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/6 18:03
 * E-mail：yangzongbin@si-top.com
 */
@Module
class IncreHistoryModule {


    private final IncreHistoryContract.View mView;

    IncreHistoryModule(IncreHistoryContract.View view) {
        mView = view;
    }

    @Provides
    IncreHistoryContract.View provideIncreHistoryContractView() {
        return mView;
    }

}