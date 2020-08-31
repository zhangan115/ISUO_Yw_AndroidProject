package com.sito.customer.view.pass;



import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/2.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = ForgePassModule.class)
interface ForgePassComponent {
    void inject(ForgePassActivity activity);
}
