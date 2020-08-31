package com.sito.evpro.inspection.view.energy;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/6/22.
 */
@Module
class EnergyListModule {

    private final EnergyListContract.View mView;

    EnergyListModule(EnergyListContract.View view) {
        mView = view;
    }

    @Provides
    EnergyListContract.View provideEnergyContractView() {
        return mView;
    }

}