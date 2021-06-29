package com.isuo.yw2application.mode;

import android.content.Context;

import com.isuo.yw2application.mode.work.DaggerWorkRepositoryComponent;
import com.isuo.yw2application.mode.work.WorkRepository;
import com.isuo.yw2application.view.ApplicationModule;


/**
 * 管理mode reposition
 * Created by zhangan on 2018/3/16.
 */

public class Injection {
    private static Injection injection;

    private Injection() {

    }

    public static Injection getInstance() {
        if (injection == null) {
            injection = new Injection();
        }
        return injection;
    }

    private WorkRepository workRepository;

    public WorkRepository getWorkRepository(Context context) {
        if (workRepository == null) {
            workRepository = DaggerWorkRepositoryComponent.builder()
                    .applicationModule(new ApplicationModule(context))
                    .build().getRepository();
        }
        return workRepository;
    }
}
