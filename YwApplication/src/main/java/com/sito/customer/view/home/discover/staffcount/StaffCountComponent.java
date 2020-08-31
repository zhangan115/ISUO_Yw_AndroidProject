package com.sito.customer.view.home.discover.staffcount;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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