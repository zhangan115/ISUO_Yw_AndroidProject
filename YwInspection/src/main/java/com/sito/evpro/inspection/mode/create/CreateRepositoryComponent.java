package com.sito.evpro.inspection.mode.create;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 巡检 component
 * <p>
 * Created by zhangan on 2017-06-22.
 */
@Singleton
@Component()
public interface CreateRepositoryComponent {

    CreateRepository getRepository();
}
