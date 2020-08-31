package com.sito.evpro.inspection.view.increment;

import com.sito.evpro.inspection.mode.commitinfo.CommitRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/25.
 */
@FragmentScoped
@Component(dependencies = CommitRepositoryComponent.class, modules = IncrementModule.class)
interface IncrementComponent {
    void inject(IncrementActivity activity);
}
