package com.sito.customer.view.home.discover.equiplist;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/15 12:34
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