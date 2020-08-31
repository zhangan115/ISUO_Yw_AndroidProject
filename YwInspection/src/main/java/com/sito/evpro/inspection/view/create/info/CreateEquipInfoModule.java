package com.sito.evpro.inspection.view.create.info;

import dagger.Module;
import dagger.Provides;

/**
 * 选择设备区域，设备类型
 * Created by zhangan on 2017/9/30.
 */
@Module
class CreateEquipInfoModule {
    private final CreateEquipInfoContract.View mView;

    public CreateEquipInfoModule(CreateEquipInfoContract.View mView) {
        this.mView = mView;
    }

    @Provides
    CreateEquipInfoContract.View provideCreateEquipContractView() {
        return mView;
    }
}
