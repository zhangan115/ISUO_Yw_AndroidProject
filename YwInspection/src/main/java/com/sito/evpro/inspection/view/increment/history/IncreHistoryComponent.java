package com.sito.evpro.inspection.view.increment.history;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Yangzb on 2017/7/6 18:03
 * E-mail：yangzongbin@si-top.com
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = IncreHistoryModule.class)
interface IncreHistoryComponent {
    void inject(IncreHistoryActivity activity);
}