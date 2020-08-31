package com.sito.evpro.inspection.view.home;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/5 18:07
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = HomeModule.class)
interface HomeComponent {
    void inject(HomeActivity activity);
}