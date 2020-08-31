package com.sito.customer.view.home.discover.equiplist;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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