package com.sito.evpro.inspection.view.equipment.equiplist;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/18 15:50
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = EquipListModule.class)
interface EquipListComponent {
    void inject(EquipListActivity activity);
}