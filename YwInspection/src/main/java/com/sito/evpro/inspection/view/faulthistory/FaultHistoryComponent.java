package com.sito.evpro.inspection.view.faulthistory;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/25.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = FaultHistoryModule.class)
interface FaultHistoryComponent {
    void inject(FaultHistoryActivity activity);
}
