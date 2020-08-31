package com.sito.evpro.inspection.view.repair.inspection.inspectdetial;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@Module
class InspectionDetailModule {


    private final InspectionDetailContract.View mView;

    InspectionDetailModule(InspectionDetailContract.View view) {
        mView = view;
    }

    @Provides
    InspectionDetailContract.View provideInspecDetialContractView() {
        return mView;
    }

}