package com.isuo.yw2application.view.main.task.inspection.detial;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

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