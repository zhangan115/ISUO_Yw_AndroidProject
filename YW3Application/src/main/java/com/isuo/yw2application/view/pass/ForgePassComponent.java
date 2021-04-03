package com.isuo.yw2application.view.pass;



import  com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import  com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/2.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = ForgePassModule.class)
interface ForgePassComponent {
    void inject(ForgePassActivity activity);
}
