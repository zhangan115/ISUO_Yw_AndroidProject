package com.sito.customer.view.home.news.equipalarm;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

import dagger.Component;

/**
 * 作者：Yangzb on 2017/6/30 11:03
 * 邮箱：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = EquipAlarmModule.class)
interface EquipAlarmComponent {
    void inject(EquipAlarmActivity activity);
}