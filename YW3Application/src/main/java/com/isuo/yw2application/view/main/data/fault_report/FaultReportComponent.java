package com.isuo.yw2application.view.main.data.fault_report;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/4 14:49
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = FaultReportModule.class)
interface FaultReportComponent {
    void inject(FaultReportActivity activity);
}