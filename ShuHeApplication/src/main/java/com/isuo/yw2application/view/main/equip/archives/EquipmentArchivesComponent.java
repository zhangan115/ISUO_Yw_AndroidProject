package com.isuo.yw2application.view.main.equip.archives;


import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * 设备档案
 * Created by Administrator on 2017/6/22.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = EquipmentArchivesModule.class)
interface EquipmentArchivesComponent {

    void inject(EquipmentArchivesActivity activity);
}
