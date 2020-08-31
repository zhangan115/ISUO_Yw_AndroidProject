package com.sito.evpro.inspection.view.create.info;

import com.sito.evpro.inspection.mode.create.CreateRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * 选择设备区域，设备类型
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = CreateRepositoryComponent.class, modules = CreateEquipInfoModule.class)
interface CreateEquipInfoComponent {

    void inject(CreateEquipInfoActivity activity);
}
