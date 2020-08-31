package com.sito.customer.view.home.work.inspection.detial;

import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = InspectDetailModule.class)
interface InspectDetailComponent {
    void inject(InspectDetailActivity activity);
}