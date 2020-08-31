package com.sito.customer.view.home.work.inspection.detial;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@Module
class InspectDetailModule {

    private final InspectDetailContract.View mView;

    InspectDetailModule(InspectDetailContract.View view) {
        mView = view;
    }

    @Provides
    InspectDetailContract.View provideInspectDetailContractView() {
        return mView;
    }

}