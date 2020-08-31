package com.sito.customer.mode.generate;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhangan on 2017-07-17.
 */
@Singleton
@Component()
public interface GenerateRepositoryComponent {

    GenerateRepository getRepository();
}
