package com.sito.evpro.inspection.view.repair.inspection.inspectdetial;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = InspectionDetailModule.class)
interface InspectionDetailComponent {
    void inject(InspectionDetailActivity activity);
}