package com.isuo.yw2application.mode.fault;

import com.isuo.yw2application.view.ApplicationModule;

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
