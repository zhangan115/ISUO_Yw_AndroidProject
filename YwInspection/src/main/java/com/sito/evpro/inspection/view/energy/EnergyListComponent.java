package com.sito.evpro.inspection.view.energy;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = EnergyListModule.class)
interface EnergyListComponent {
    void inject(EnergyListActivity activity);
}
