package com.isuo.yw2application.view.login;



import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * login dagger component
 * Created by zhangan on 2017-02-27.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = LoginModule.class)
interface LoginComponent {

    void inject(LoginActivity activity);
}
