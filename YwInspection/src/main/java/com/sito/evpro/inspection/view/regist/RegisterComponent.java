package com.sito.evpro.inspection.view.regist;



import com.sito.evpro.inspection.mode.inspection.InspectionRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/2.
 */
@FragmentScoped
@Component(dependencies = InspectionRepositoryComponent.class, modules = RegisterModule.class)
interface RegisterComponent {
    void inject(RegisterActivity activity);
}
