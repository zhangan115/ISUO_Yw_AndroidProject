package com.isuo.yw2application.mode.work;



import com.isuo.yw2application.view.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 巡检 component
 * <p>
 * Created by zhangan on 2017-06-22.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface WorkRepositoryComponent {

    WorkRepository getRepository();
}
