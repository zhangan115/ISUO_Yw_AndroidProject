package com.sito.evpro.inspection.view.equipment.archives;

import dagger.Module;
import dagger.Provides;

/**
 * 设备档案
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
