package com.sito.evpro.inspection.view.repair.inspection.data;


import com.sito.evpro.inspection.mode.inspection.work.InspectionWorkRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/17 19:35
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionWorkRepositoryComponent.class, modules = InspectionDataModule.class)
interface InspectionDataComponent {
    void inject(InspectionDataActivity activity);
}