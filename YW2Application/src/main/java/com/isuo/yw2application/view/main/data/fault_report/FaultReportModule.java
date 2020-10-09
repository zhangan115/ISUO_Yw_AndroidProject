package com.isuo.yw2application.view.main.data.fault_report;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yangzb on 2017/7/4 14:49
 * E-mail：yangzongbin@si-top.com
 */
@Module
class FaultReportModule {


    private final FaultReportContract.View mView;

    FaultReportModule(FaultReportContract.View view) {
        mView = view;
    }

    @Provides
    FaultReportContract.View provideFaultReportContractView() {
        return mView;
    }

}