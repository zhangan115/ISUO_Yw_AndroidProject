package com.sito.evpro.inspection.mode.inspection.work;

import com.sito.evpro.inspection.view.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 上传巡检数据 component
 * <p>
 * Created by zhangan on 2017-06-22.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface InspectionWorkRepositoryComponent {

    InspectionWorkRepository getRepository();
}
