package com.sito.evpro.inspection.view.create.equipment;

import dagger.Module;
import dagger.Provides;

/**
 * 创建设备/修改设备信息
 * Created by zhangan on 2017/9/30.
 */
@Module
class CreateEquipModule {
    private final CreateEquipContract.View mView;

    public CreateEquipModule(CreateEquipContract.View mView) {
        this.mView = mView;
    }

    @Provides
    CreateEquipContract.View provideCreateEquipContractView() {
        return mView;
    }
}
