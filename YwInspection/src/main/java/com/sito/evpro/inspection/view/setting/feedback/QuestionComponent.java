package com.sito.evpro.inspection.view.setting.feedback;


import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/14.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = QuestionModule.class)
interface QuestionComponent {
    void inject(QuestionActivity activity);
}
