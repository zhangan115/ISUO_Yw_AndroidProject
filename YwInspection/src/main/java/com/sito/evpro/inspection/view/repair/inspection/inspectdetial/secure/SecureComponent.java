package com.sito.evpro.inspection.view.repair.inspection.inspectdetial.secure;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/23 16:52
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = SecureModule.class)
interface SecureComponent {
    void inject(SecureActivity activity);
}