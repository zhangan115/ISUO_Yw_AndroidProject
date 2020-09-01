package com.isuo.yw2application.mode.count;

import com.isuo.yw2application.view.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhangan on 2017-07-19.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface CountRepositoryComponent {

    CountRepository getRepository();
}
