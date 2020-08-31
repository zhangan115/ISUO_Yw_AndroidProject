package com.sito.evpro.inspection.view.repair.inspection.data;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@Module
class InspectionDataModule {


    private final InspectionDataContract.View mView;

    InspectionDataModule(InspectionDataContract.View view) {
        mView = view;
    }

    @Provides
    InspectionDataContract.View provideInspecDetialContractView() {
        return mView;
    }

}