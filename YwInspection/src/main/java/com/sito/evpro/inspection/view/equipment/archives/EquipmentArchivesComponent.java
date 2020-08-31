package com.sito.evpro.inspection.view.equipment.archives;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * 设备档案
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = EquipmentArchivesModule.class)
interface EquipmentArchivesComponent {

    void inject( EquipmentArchivesActivity activity);
}
