package com.sito.customer.view.home.mine.feedback;


import com.sito.customer.mode.customer.CustomerRepositoryComponent;
import com.sito.customer.view.FragmentScoped;

import dagger.Component;

/**
 * Created by Administrator on 2017/6/14.
 */
@FragmentScoped
@Component(dependencies = CustomerRepositoryComponent.class, modules = QuestionModule.class)
interface QuestionComponent {
    void inject(QuestionActivity activity);
}
