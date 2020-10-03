package com.isuo.yw2application.view.main.device.search;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/17 14:54
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = EquipSearchModule.class)
interface EquipSearchComponent {
    void inject(EquipSearchActivity activity);
}