package com.sito.evpro.inspection.view.equipment.equiplist.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/10 18:15
 * E-mail：yangzongbin@si-top.com
 */
@Module
class EquipSearchModule {


    private final EquipSearchContract.View mView;

    EquipSearchModule(EquipSearchContract.View view) {
        mView = view;
    }

    @Provides
    EquipSearchContract.View provideEquipSearchContractView() {
        return mView;
    }

}