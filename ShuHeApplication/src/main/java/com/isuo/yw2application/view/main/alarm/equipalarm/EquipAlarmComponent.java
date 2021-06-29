package com.isuo.yw2application.view.main.alarm.equipalarm;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

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