package com.isuo.yw2application.view.main.data.staff_time;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/4 16:03
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = StaffTimeModule.class)
interface StaffTimeComponent {
    void inject(StaffTimeActivity activity);
}