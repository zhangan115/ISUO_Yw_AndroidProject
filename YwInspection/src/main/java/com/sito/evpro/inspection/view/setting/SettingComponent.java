package com.sito.evpro.inspection.view.setting;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/4 13:35
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = SettingModule.class)
interface SettingComponent {
    void inject(SettingActivity activity);
}