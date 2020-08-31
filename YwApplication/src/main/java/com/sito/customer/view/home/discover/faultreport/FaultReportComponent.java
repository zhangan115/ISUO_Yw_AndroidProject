package com.sito.customer.view.home.discover.faultreport;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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