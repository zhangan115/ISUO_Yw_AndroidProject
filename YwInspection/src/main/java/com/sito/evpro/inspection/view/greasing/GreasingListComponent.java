package com.sito.evpro.inspection.view.greasing;

import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * 注油管理
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = GreasingListModule.class)
interface GreasingListComponent {
    void inject(GreasingListActivity activity);
}
