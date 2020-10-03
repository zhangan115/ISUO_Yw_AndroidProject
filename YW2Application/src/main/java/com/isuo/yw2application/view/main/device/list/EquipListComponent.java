package com.isuo.yw2application.view.main.device.list;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/15 12:34
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = EquipListModule.class)
interface EquipListComponent {
    void inject(EquipListActivity activity);
}