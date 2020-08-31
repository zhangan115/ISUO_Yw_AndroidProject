package com.sito.customer.view.home.news.equipalarm;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：Yangzb on 2017/6/30 10:59
 * 邮箱：yangzongbin@si-top.com
 */
@Module
class EquipAlarmModule {


    private final EquipAlarmContract.View mView;

    EquipAlarmModule(EquipAlarmContract.View view) {
        mView = view;
    }

    @Provides
    EquipAlarmContract.View provideEquipAlarmContractView() {
        return mView;
    }

}