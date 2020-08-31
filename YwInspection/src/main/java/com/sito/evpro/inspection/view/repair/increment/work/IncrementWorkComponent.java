package com.sito.evpro.inspection.view.repair.increment.work;

import com.sito.evpro.inspection.mode.commitinfo.CommitRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * 专项工作详情
 * Created by zhangan on 2017/10/10.
 */
@FragmentScoped
@Component(dependencies = CommitRepositoryComponent.class, modules = IncrementWorkModule.class)
interface IncrementWorkComponent {
    void inject(IncrementWorkActivity activity);
}


