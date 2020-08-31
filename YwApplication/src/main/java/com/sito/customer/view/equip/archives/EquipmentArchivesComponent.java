package com.sito.customer.view.equip.archives;


import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

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
