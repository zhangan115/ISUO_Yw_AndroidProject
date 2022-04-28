package com.isuo.yw2application.view.main.equip.archives;

import dagger.Module;
import dagger.Provides;

/**
 * 对象档案
 * Created by zhangan on 2017/10/12.
 */
@Module
 class EquipmentArchivesModule {

    private final EquipmentArchivesContract.View mView;

    public EquipmentArchivesModule(EquipmentArchivesContract.View mView) {
        this.mView = mView;
    }

    @Provides
    EquipmentArchivesContract.View provideCreateEquipContractView() {
        return mView;
    }
}
