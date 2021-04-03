package com.isuo.yw2application.view.main.data.fault_time;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/4 15:34
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = FaultTimeModule.class)
interface FaultTimeComponent {
    void inject(FaultTimeActivity activity);
}