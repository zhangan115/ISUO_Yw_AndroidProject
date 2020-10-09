package com.isuo.yw2application.view.main.data.staff_count;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/4 16:07
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = StaffCountModule.class)
interface StaffCountComponent {
    void inject(StaffCountActivity activity);
}