package com.sito.evpro.inspection.view.repair.increment.work;

import dagger.Module;
import dagger.Provides;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */
@Module
class IncrementWorkModule {
    private final IncrementWorkContract.View mView;

    IncrementWorkModule(IncrementWorkContract.View mView) {
        this.mView = mView;
    }

    @Provides
    IncrementWorkContract.View provideCreateEquipContractView() {
        return mView;
    }
}
