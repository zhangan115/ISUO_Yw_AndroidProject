package com.sito.evpro.inspection.view.equipment.equiplist.search;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/10 18:16
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = EquipSearchModule.class)
interface EquipSearchComponent {
    void inject(EquipSearchActivity activity);
}