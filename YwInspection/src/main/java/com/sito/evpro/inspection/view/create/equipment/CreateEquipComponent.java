package com.sito.evpro.inspection.view.create.equipment;

import com.sito.evpro.inspection.mode.create.CreateRepositoryComponent;
import com.sito.evpro.inspection.view.FragmentScoped;

import dagger.Component;

/**
 * 创建设备/修改设备信息
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = CreateRepositoryComponent.class, modules = CreateEquipModule.class)
interface CreateEquipComponent {

    void inject(CreateEquipmentActivity activity);
}
