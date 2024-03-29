package com.sito.evpro.inspection.mode.employee;

import com.sito.evpro.inspection.view.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 巡检 component
 * <p>
 * Created by zhangan on 2017-06-22.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface EmployeeRepositoryComponent {

    EmployeeRepository getRepository();
}
