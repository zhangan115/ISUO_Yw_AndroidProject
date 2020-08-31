package com.sito.evpro.inspection.view.fault;

import com.sito.evpro.inspection.mode.commitinfo.CommitRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/25.
 */
@FragmentScoped
@Component(dependencies = CommitRepositoryComponent.class, modules = FaultModule.class)
interface FaultComponent {
    void inject(FaultActivity activity);
}
