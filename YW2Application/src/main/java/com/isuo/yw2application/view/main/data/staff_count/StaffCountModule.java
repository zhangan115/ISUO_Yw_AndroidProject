package com.isuo.yw2application.view.main.data.staff_count;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 16:06
 * E-mail：yangzongbin@si-top.com
 */
@Module
class StaffCountModule {


    private final StaffCountContract.View mView;

    StaffCountModule(StaffCountContract.View view) {
        mView = view;
    }

    @Provides
    StaffCountContract.View provideStaffCountContractView() {
        return mView;
    }

}