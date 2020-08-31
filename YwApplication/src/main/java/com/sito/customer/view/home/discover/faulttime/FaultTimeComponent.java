package com.sito.customer.view.home.discover.faulttime;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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