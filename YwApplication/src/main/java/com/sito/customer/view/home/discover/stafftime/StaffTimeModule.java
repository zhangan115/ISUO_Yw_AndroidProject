package com.sito.customer.view.home.discover.stafftime;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 16:03
 * E-mail：yangzongbin@si-top.com
 */
@Module
class StaffTimeModule {


    private final StaffTimeContract.View mView;

    StaffTimeModule(StaffTimeContract.View view) {
        mView = view;
    }

    @Provides
    StaffTimeContract.View provideStaffTimeContractView() {
        return mView;
    }

}