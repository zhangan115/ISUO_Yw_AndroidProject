package com.sito.evpro.inspection.view.equipment.equiplist;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/18 15:50
 * E-mail：yangzongbin@si-top.com
 */
@Module
class EquipListModule {


    private final EquipListContract.View mView;

    EquipListModule(EquipListContract.View view) {
        mView = view;
    }

    @Provides
    EquipListContract.View provideEquipListContractView() {
        return mView;
    }

}