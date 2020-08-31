package com.sito.customer.mode.fault;

import com.sito.customer.view.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhangan on 2017-07-17.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface FaultRepositoryComponent {

    FaultRepository getRepository();
}
