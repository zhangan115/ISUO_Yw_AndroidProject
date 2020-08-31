package com.sito.customer.view.home.discover.equiplist.search;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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