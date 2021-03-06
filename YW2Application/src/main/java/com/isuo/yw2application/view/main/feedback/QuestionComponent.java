package com.isuo.yw2application.view.main.feedback;


import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import com.isuo.yw2application.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/14.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = QuestionModule.class)
interface QuestionComponent {
    void inject(QuestionActivity activity);
}
