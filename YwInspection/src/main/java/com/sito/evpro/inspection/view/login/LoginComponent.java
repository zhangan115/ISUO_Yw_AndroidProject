package com.sito.evpro.inspection.view.login;


import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * login dagger component
 * Created by zhangan on 2017-02-27.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = LoginModule.class)
interface LoginComponent {

    void inject(LoginActivity activity);
}
