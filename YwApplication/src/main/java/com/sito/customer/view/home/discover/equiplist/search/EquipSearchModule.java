package com.sito.customer.view.home.discover.equiplist.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/17 14:53
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